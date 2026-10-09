package com.example.eventbook_backend.dto.booking;

import com.example.eventbook_backend.enums.BookingStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class BookingResponse {

    private Long id;
    private String bookingNumber;
    private Long userId;
    private Long movieShowId;
    private BigDecimal totalAmount;
    private BookingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expiresAt;
    private List<BookingSeatResponse> bookingSeats;
}
