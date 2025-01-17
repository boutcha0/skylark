package com.crudops.skylark.service;

import com.crudops.skylark.model.Order;
import com.crudops.skylark.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOrderConfirmationEmail(Order order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(order.getInfo().getEmail());
        message.setSubject("Order Confirmation #" + order.getId());

        StringBuilder emailBody = new StringBuilder();
        emailBody.append("Thank you for your order!\n\n");
        emailBody.append("Order Details:\n");
        emailBody.append("Order Number: ").append(order.getId()).append("\n");
        emailBody.append("Order Date: ").append(order.getOrderDate()).append("\n\n");

        emailBody.append("Items:\n");
        for (OrderItem item : order.getOrderItems()) {
            emailBody.append("- ")
                    .append(item.getProduct().getName())
                    .append(" (Quantity: ").append(item.getQuantity()).append(")")
                    .append(" - $").append(String.format("%.2f", item.getTotalAmount()))
                    .append("\n");
        }

        emailBody.append("\nTotal Amount: $").append(String.format("%.2f", order.getTotalAmount()));

        if (order.getShippingAddress() != null) {
            emailBody.append("\n\nShipping Address:\n");
            emailBody.append(order.getShippingAddress().getStreetAddress()).append("\n");
            emailBody.append(order.getShippingAddress().getCity()).append(", ");
            emailBody.append(order.getShippingAddress().getState()).append(" ");
            emailBody.append(order.getShippingAddress().getPostalCode()).append("\n");
            emailBody.append(order.getShippingAddress().getCountry());
        }

        emailBody.append("\n\nIf you have any questions, please contact us.");

        message.setText(emailBody.toString());
        mailSender.send(message);
    }
}