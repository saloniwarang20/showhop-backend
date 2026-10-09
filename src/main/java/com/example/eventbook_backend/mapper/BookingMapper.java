package com.example.eventbook_backend.mapper;

import com.example.eventbook_backend.dto.booking.BookingResponse;
import com.example.eventbook_backend.dto.booking.BookingSeatResponse;
import com.example.eventbook_backend.entity.Booking;
import com.example.eventbook_backend.entity.BookingSeat;
import com.example.eventbook_backend.repository.BookingSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class BookingMapper {

    private final BookingSeatMapper bookingSeatMapper;

    public BookingResponse toResponse(Booking booking, List<BookingSeat> bookingSeats){

        List<BookingSeatResponse> seatResponses = bookingSeats.stream()
                .map(bookingSeatMapper::toResponse)
                .toList();

        LocalDateTime expiresAt = bookingSeats.stream()
                .map(bookingSeat -> bookingSeat.getShowSeat().getHeldUntil())
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        return BookingResponse.builder()
                .id(booking.getId())
                .bookingNumber(booking.getBookingNumber())
                .userId(booking.getUser().getId())
                .movieShowId(booking.getMovieShow().getId())
                .totalAmount(booking.getTotalAmount())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .expiresAt(expiresAt)
                .bookingSeats(seatResponses)
                .build();
    }
}
