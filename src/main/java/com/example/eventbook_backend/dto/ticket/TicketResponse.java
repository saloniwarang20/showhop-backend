package com.example.eventbook_backend.dto.ticket;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Setter
@Getter
@Builder
public class TicketResponse {

    private Long id;
    private String ticketNumber;
    private String bookingNumber;
    private String movieTitle;
    private String theatreName;
    private LocalDate showDate;
    private LocalTime showTime;
    private List<String> seats;
    private BigDecimal amount;
}
