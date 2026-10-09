package com.example.eventbook_backend.mapper;

import com.example.eventbook_backend.dto.booking.BookingSeatResponse;
import com.example.eventbook_backend.entity.BookingSeat;
import org.springframework.stereotype.Component;

@Component
public class BookingSeatMapper {

    public BookingSeatResponse toResponse(BookingSeat bookingSeat){
        return BookingSeatResponse.builder()
                .id(bookingSeat.getId())
                .bookingId(bookingSeat.getBooking().getId())
                .showSeatId(bookingSeat.getShowSeat().getId())
                .seatNumber(bookingSeat.getShowSeat().getSeat().getSeatNumber())
                .rowNumber(bookingSeat.getShowSeat().getSeat().getRowNumber())
                .price(bookingSeat.getPrice())
                .build();
    }
}
