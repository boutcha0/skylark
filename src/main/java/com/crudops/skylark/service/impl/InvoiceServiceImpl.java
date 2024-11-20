package com.crudops.skylark.service.impl;

import com.crudops.skylark.DTO.InvoiceDTO;
import com.crudops.skylark.mapper.InvoiceMapper;
import com.crudops.skylark.mapper.OrdersMapper;
import com.crudops.skylark.model.Invoice;
import com.crudops.skylark.repository.InvoiceRepository;
import com.crudops.skylark.repository.OrdersRepository;
import com.crudops.skylark.service.InvoiceService;
import jakarta.transaction.Transactional;
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
}

