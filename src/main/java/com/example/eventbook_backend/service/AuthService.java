package com.example.eventbook_backend.service;

import com.example.eventbook_backend.dto.auth.AuthResponse;
import com.example.eventbook_backend.dto.auth.LoginRequest;
import com.example.eventbook_backend.dto.auth.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

}
