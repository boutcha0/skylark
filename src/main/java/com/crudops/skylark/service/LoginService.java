package com.crudops.skylark.service;

import com.crudops.skylark.config.JwtTokenUtil;
import com.crudops.skylark.exception.InfosNotFoundException;
import com.crudops.skylark.model.Info;
import com.crudops.skylark.repository.InfosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final InfosRepository infosRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public String authenticateAndGetToken(String email, String rawPassword) {
        Info user = infosRepository.findByEmail(email)
                .orElseThrow(() -> new InfosNotFoundException("No user found with email: " + email));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        // Use the new generateTokenWithId method
        return jwtTokenUtil.generateTokenWithId(user.getEmail(), user.getId());
    }

    public Info getUserByEmail(String email) {
        return infosRepository.findByEmail(email)
                .orElseThrow(() -> new InfosNotFoundException("No user found with email: " + email));
    }

    public boolean validateLogin(String email, String rawPassword) {
        return infosRepository.findByEmail(email)
                .map(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
                .orElse(false);
    }
}