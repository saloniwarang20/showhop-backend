package com.example.eventbook_backend.mapper;

import com.example.eventbook_backend.dto.seat.SeatResponse;
import com.example.eventbook_backend.entity.Seat;
import org.springframework.stereotype.Component;

@Component
public class SeatMapper {

    public SeatResponse toResponse(Seat seat){
        return SeatResponse.builder()
                .id(seat.getId())
                .seatNumber(seat.getSeatNumber())
                .rowNumber(seat.getRowNumber())
                .seatType(seat.getSeatType())
                .price(seat.getPrice())
                .screenId(seat.getScreen().getId())
                .createdAt(seat.getCreatedAt())
                .updatedAt(seat.getUpdatedAt())
                .build();
    }
}
