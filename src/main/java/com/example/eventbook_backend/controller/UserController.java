package com.example.eventbook_backend.controller;

import com.example.eventbook_backend.dto.user.UserResponse;
import com.example.eventbook_backend.entity.User;
import com.example.eventbook_backend.security.CustomerUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication){
        CustomerUserDetails customerUserDetails = (CustomerUserDetails) authentication.getPrincipal();

        User user = customerUserDetails.getUser();

        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .profileImage(user.getProfileImage())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin-test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getAdminUser(){
        return ResponseEntity.noContent().build();
    }
}
