package com.example.eventbook_backend.service.impl;

import com.example.eventbook_backend.dto.auth.AuthResponse;
import com.example.eventbook_backend.dto.auth.LoginRequest;
import com.example.eventbook_backend.dto.auth.RegisterRequest;
import com.example.eventbook_backend.dto.user.UserResponse;
import com.example.eventbook_backend.entity.User;
import com.example.eventbook_backend.enums.AuthProvider;
import com.example.eventbook_backend.enums.Role;
import com.example.eventbook_backend.exception.InvalidCredentialsException;
import com.example.eventbook_backend.exception.ResourceAlreadyExistsException;
import com.example.eventbook_backend.repository.UserRepository;
import com.example.eventbook_backend.security.CustomerUserDetails;
import com.example.eventbook_backend.security.JwtService;
import com.example.eventbook_backend.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        if(userRepository.existsByEmail(email)){
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .authProvider(AuthProvider.LOCAL)
                .role(Role.USER)
                .enabled(true)
                .build();

        User savedUser = userRepository.save(user);

        CustomerUserDetails userDetails = new CustomerUserDetails(savedUser);

        String token = jwtService.generateToken(userDetails);

        return buildAuthResponse(savedUser, token);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            request.getPassword()
                    )
            );

            CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();

            User user = userDetails.getUser();

            String token = jwtService.generateToken(userDetails);

            return buildAuthResponse(user, token);

        }catch (BadCredentialsException e){
            throw new InvalidCredentialsException("Invalid email or password");
        }

    }

    private AuthResponse buildAuthResponse(User user, String token){
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .profileImage(user.getProfileImage())
                .build();

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .user(userResponse)
                .build();
    }
}
