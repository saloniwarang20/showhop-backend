package com.example.eventbook_backend.dto.showSeat;

import com.example.eventbook_backend.enums.SeatStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class ShowSeatResponse {

    private Long id;
    private Long movieShowId;
    private Long seatId;
    private String seatNumber;
    private String rowNumber;
    private SeatStatus status;
    private BigDecimal price;
    private LocalDateTime heldUntil;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
