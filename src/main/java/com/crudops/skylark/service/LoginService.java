// src/main/java/com/crudops/skylark/service/LoginService.java
package com.crudops.skylark.service;

import com.crudops.skylark.exception.InfosNotFoundException;
import com.crudops.skylark.model.Info;
import com.crudops.skylark.repository.InfosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final InfosRepository infosRepository;

    // Validate login based on email
    public boolean validateEmailLogin(String email) {
        // Check if the email exists in the database
        Info info = infosRepository.findByEmail(email)
                .orElseThrow(() -> new InfosNotFoundException("No user found with email: " + email));
        return info != null; // If the email exists, return true, otherwise false
    }
}
