package com.crudops.skylark.service;

import com.crudops.skylark.model.Order;
import com.crudops.skylark.config.EmailConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor

public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private final EmailConfig emailConfig;


    public void sendOrderConfirmationEmail(Order order) throws MessagingException {
        Context context = new Context();
        context.setVariable("order", order);

        String htmlContent = templateEngine.process("order-confirmation", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(order.getInfo().getEmail());
        helper.setSubject("Order Confirmation #" + order.getId());
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }



    public void sendContactFormEmail(String from, String subject, String message) throws MessagingException {
        Context context = new Context();
        context.setVariable("email", from);
        context.setVariable("subject", subject);
        context.setVariable("message", message);

        String htmlContent = templateEngine.process("contact-form", context);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom(from);
        helper.setTo(emailConfig.getAdminEmail());
        helper.setSubject("Contact Form: " + subject);
        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }
}