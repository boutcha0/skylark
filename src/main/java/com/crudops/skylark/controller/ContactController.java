package com.crudops.skylark.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crudops.skylark.service.EmailService;
import com.crudops.skylark.model.ContactFormRequest;


import jakarta.mail.MessagingException;


@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<?> submitContactForm(@RequestBody @Valid ContactFormRequest request) {
        try {
            emailService.sendContactFormEmail(
                    request.getEmail(),
                    request.getSubject(),
                    request.getMessage()
            );
            return ResponseEntity.ok().build();
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send email: " + e.getMessage());
        }
    }
}