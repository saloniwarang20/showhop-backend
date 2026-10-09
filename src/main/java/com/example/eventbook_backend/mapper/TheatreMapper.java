package com.example.eventbook_backend.mapper;

import com.example.eventbook_backend.dto.theatre.TheatreResponse;
import com.example.eventbook_backend.entity.Theatre;
import org.springframework.stereotype.Component;

@Component
public class TheatreMapper {

    public TheatreResponse toResponse(Theatre theatre){
        return TheatreResponse.builder()
                .id(theatre.getId())
                .name(theatre.getName())
                .address(theatre.getAddress())
                .city(theatre.getCity())
                .state(theatre.getState())
                .pincode(theatre.getPincode())
                .imageUrl(theatre.getImageUrl())
                .createdAt(theatre.getCreatedAt())
                .updatedAt(theatre.getUpdatedAt())
                .build();
    }
}
