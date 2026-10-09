package com.example.eventbook_backend.dto.booking;

import com.example.eventbook_backend.enums.BookingStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
public class BookingRequest {

    private Long movieShowId;
    private List<Long> showSeatIds;

}
