package com.example.eventbook_backend.dto.seat;

import com.example.eventbook_backend.enums.SeatType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class SeatResponse {

    private Long id;
    private String seatNumber;
    private String rowNumber;
    private SeatType seatType;
    private BigDecimal price;
    private Long screenId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
