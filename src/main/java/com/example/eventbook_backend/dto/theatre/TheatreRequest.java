package com.example.eventbook_backend.dto.theatre;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TheatreRequest {

    private String name;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String imageUrl;
}
