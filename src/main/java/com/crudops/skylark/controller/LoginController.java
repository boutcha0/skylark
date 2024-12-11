// src/main/java/com/crudops/skylark/controller/LoginController.java
package com.crudops.skylark.controller;

import com.crudops.skylark.DTO.LoginRequest;
import com.crudops.skylark.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        // Check if the email exists in the database
        boolean isEmailValid = loginService.validateEmailLogin(loginRequest.getEmail());

        if (isEmailValid) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid email");
        }
    }
}
