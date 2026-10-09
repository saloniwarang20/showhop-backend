package com.example.eventbook_backend.dto.user;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    @Size(max=100)
    private String firstName;

    @Size(max=100)
    private String lastName;

    @Size(max=20)
    private String phoneNumber;

    private String profileImage;
}
