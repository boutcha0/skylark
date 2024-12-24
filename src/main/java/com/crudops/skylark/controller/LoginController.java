package com.crudops.skylark.controller;

import com.crudops.skylark.DTO.InfosDTO;
import com.crudops.skylark.DTO.LoginRequest;
import com.crudops.skylark.DTO.LoginResponse;
import com.crudops.skylark.config.JwtTokenUtil;
import com.crudops.skylark.exception.InfosValidationException;
import com.crudops.skylark.service.LoginService;
import com.crudops.skylark.service.impl.InfosServiceImpl;
import com.crudops.skylark.service.impl.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;
    private final JwtTokenUtil jwtTokenUtil;
    private final InfosServiceImpl infosServiceImpl;
    private final TokenBlacklistService tokenBlacklistService;

    @Autowired
    public LoginController(LoginService loginService, JwtTokenUtil jwtTokenUtil,
                           InfosServiceImpl infosServiceImpl, TokenBlacklistService tokenBlacklistService) {
        this.loginService = loginService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.infosServiceImpl = infosServiceImpl;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    // 1. Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        boolean isLoginValid = loginService.validateLogin(loginRequest.getEmail(), loginRequest.getPassword());

        if (isLoginValid) {
            String token = jwtTokenUtil.generateToken(loginRequest.getEmail());
            Long userId = infosServiceImpl.getIdByEmail(loginRequest.getEmail()); // Retrieve the user's ID

            return ResponseEntity.ok(new LoginResponse(token, "Login successful", userId));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(null, "Invalid credentials", null));
        }
    }

    // 2. Register Endpoint
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody InfosDTO infosDTO) {
        try {
            InfosDTO createdUser = infosServiceImpl.createInfos(infosDTO);
            String token = jwtTokenUtil.generateToken(createdUser.getEmail());
            Long userId = infosServiceImpl.getIdByEmail(createdUser.getEmail()); // Retrieve the user's ID

            return ResponseEntity.ok(new LoginResponse(token, "Registration successful", userId));
        } catch (InfosValidationException e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse(null, e.getMessage(), null));
        }
    }

    // 3. Validate Token Endpoint
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(HttpServletRequest request) {
        String token = extractToken(request);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid token"));
        }

        try {
            boolean isValid = jwtTokenUtil.validateToken(token, jwtTokenUtil.getUsernameFromToken(token));
            return isValid
                    ? ResponseEntity.ok().body(Map.of("message", "Token is valid"))
                    : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token validation failed"));
        }
    }

    // 4. Refresh Token Endpoint
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String token = extractToken(request);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid token"));
        }

        try {
            String username = jwtTokenUtil.getUsernameFromToken(token);
            if (jwtTokenUtil.validateToken(token, username)) {
                String newToken = jwtTokenUtil.generateToken(username);
                return ResponseEntity.ok(Map.of("token", newToken));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Token refresh failed"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 5. Logout Endpoint
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = extractToken(request);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid token"));
        }

        if (tokenBlacklistService.isBlacklisted(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Token already logged out"));
        }

        tokenBlacklistService.addToBlacklist(token);

        // Clear the authentication context
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }

    // Helper method to extract token from the request
    private String extractToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return (token != null && token.startsWith("Bearer ")) ? token.substring(7) : null;
    }
}
