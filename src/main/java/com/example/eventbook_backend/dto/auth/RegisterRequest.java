package com.example.eventbook_backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "First name is required")
    @Size(max=100, message = "First name must not exceed 100 characters")
    private String firstName;

    @Size(max=100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min=8, max=100, message = "Passsword must between 8 and 100 characters")
    private String password;

    @Size(max=20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;
}
