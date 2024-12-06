package com.crudops.skylark.service.impl;

import com.crudops.skylark.DTO.InvoiceDTO;
import com.crudops.skylark.mapper.InvoiceMapper;
import com.crudops.skylark.mapper.OrdersMapper;
import com.crudops.skylark.model.Invoice;
import com.crudops.skylark.repository.InvoiceRepository;
import com.crudops.skylark.repository.OrdersRepository;
import com.crudops.skylark.service.InvoiceService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import jakarta.transaction.Transactional;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private OrdersMapper orderMapper;

    @Override
    public void generateInvoicesForCompletedOrders() {
        List<Object[]> customerOrders = ordersRepository.findCompletedOrdersGroupedByCustomer();

        for (Object[] customerOrder : customerOrders) {
            Long customerId = (Long) customerOrder[0];
            Double totalAmount = (Double) customerOrder[1];

            // Create and save an invoice
            InvoiceDTO invoiceDTO = new InvoiceDTO();
            invoiceDTO.setCustomerId(customerId);
            invoiceDTO.setTotalAmount(totalAmount);
            invoiceDTO.setInvoiceNumber("INV-" + System.currentTimeMillis());
            Invoice savedInvoice = invoiceRepository.save(invoiceMapper.toEntity(invoiceDTO));

            // Update orders with the new invoice ID
            ordersRepository.updateInvoiceIdForCustomerOrders(customerId, savedInvoice.getId());
        }
    }

    public List<InvoiceDTO> getInvoices() {
        return invoiceMapper.toDTOList(invoiceRepository.findAll());
    }


    @Override
    public byte[] generateInvoicePdf(Long invoiceId) throws Exception {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Add invoice header
        document.add(new Paragraph("Invoice")
                .setFontSize(20).setBold());
        document.add(new Paragraph("Invoice Number: " + invoice.getInvoiceNumber()));
        document.add(new Paragraph("Customer ID: " + invoice.getCustomerId()));
        document.add(new Paragraph("Total Amount: $" + invoice.getTotalAmount()));


        // Optional: Add more details or tables
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Thank you for your business!"));

        // Close document
        document.close();

        return byteArrayOutputStream.toByteArray();
    }

}
