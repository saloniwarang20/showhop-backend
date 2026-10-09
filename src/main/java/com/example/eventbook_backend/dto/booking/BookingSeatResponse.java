package com.example.eventbook_backend.dto.booking;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class BookingSeatResponse {

    private Long id;
    private Long bookingId;
    private Long showSeatId;
    private String seatNumber;
    private String rowNumber;
    private BigDecimal price;
}
