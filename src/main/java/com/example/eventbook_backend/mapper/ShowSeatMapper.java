package com.example.eventbook_backend.mapper;

import com.example.eventbook_backend.dto.showSeat.ShowSeatResponse;
import com.example.eventbook_backend.entity.ShowSeat;
import org.springframework.stereotype.Component;

@Component
public class ShowSeatMapper {

    public ShowSeatResponse toResponse(ShowSeat showSeat){
        return ShowSeatResponse.builder()
                .id(showSeat.getId())
                .movieShowId(showSeat.getMovieShow().getId())
                .seatId(showSeat.getSeat().getId())
                .seatNumber(showSeat.getSeat().getSeatNumber())
                .rowNumber(showSeat.getSeat().getRowNumber())
                .price(showSeat.getPrice())
                .status(showSeat.getStatus())
                .heldUntil(showSeat.getHeldUntil())
                .createdAt(showSeat.getCreatedAt())
                .updatedAt(showSeat.getUpdatedAt())
                .build();
    }
}
