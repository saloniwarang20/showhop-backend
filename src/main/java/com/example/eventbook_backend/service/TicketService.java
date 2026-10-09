package com.example.eventbook_backend.service;

import com.example.eventbook_backend.dto.ticket.TicketResponse;

public interface TicketService {

    TicketResponse createTicket(Long bookingId);

    TicketResponse getTicket(Long bookingId);
}
