package com.example.auditservice.controller;

import com.example.auditservice.dto.AuthRequest;
import com.example.auditservice.dto.AuthResponse;
import com.example.auditservice.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/token")
    public ResponseEntity<AuthResponse> generateToken(@RequestBody AuthRequest authRequest) {
        String email = authRequest.getEmail();
        
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new AuthResponse(null, null));
        }

        String token = jwtUtil.generateToken(email);
        
        return ResponseEntity.ok(new AuthResponse(token, email));
    }
}
