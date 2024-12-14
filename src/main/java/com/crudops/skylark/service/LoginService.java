package com.crudops.skylark.service;

import com.crudops.skylark.exception.InfosNotFoundException;
import com.crudops.skylark.model.Info;
import com.crudops.skylark.repository.InfosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final InfosRepository infosRepository;
    private final PasswordEncoder passwordEncoder;

    // Validate login based on email and password
    public boolean validateLogin(String email, String rawPassword) {
        return infosRepository.findByEmail(email)
                .map(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
                .orElse(false);
    }

    // Get user details by email for JWT generation
    public Info getUserByEmail(String email) {
        return infosRepository.findByEmail(email)
                .orElseThrow(() -> new InfosNotFoundException("No user found with email: " + email));
    }
}