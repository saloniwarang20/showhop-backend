package com.example.eventbook_backend.dto.seat;

import com.example.eventbook_backend.enums.SeatType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class SeatRequest {

    private String seatNumber;
    private String rowNumber;
    private SeatType seatType;
    private BigDecimal price;
    private Long screenId;

}
