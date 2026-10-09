package com.example.eventbook_backend.controller;

import com.example.eventbook_backend.dto.auth.AuthResponse;
import com.example.eventbook_backend.dto.auth.LoginRequest;
import com.example.eventbook_backend.dto.auth.RegisterRequest;
import com.example.eventbook_backend.dto.user.UserResponse;
import com.example.eventbook_backend.entity.User;
import com.example.eventbook_backend.security.CustomerUserDetails;
import com.example.eventbook_backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request){
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }




}
