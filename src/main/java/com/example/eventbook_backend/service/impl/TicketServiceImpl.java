package com.example.eventbook_backend.service.impl;

import com.example.eventbook_backend.dto.ticket.TicketResponse;
import com.example.eventbook_backend.entity.Booking;
import com.example.eventbook_backend.entity.BookingSeat;
import com.example.eventbook_backend.entity.Ticket;
import com.example.eventbook_backend.entity.User;
import com.example.eventbook_backend.enums.BookingStatus;
import com.example.eventbook_backend.exception.IllegalStateException;
import com.example.eventbook_backend.exception.ResourceAlreadyExistsException;
import com.example.eventbook_backend.exception.ResourceNotFoundException;
import com.example.eventbook_backend.mapper.TicketMapper;
import com.example.eventbook_backend.repository.BookingRepository;
import com.example.eventbook_backend.repository.BookingSeatRepository;
import com.example.eventbook_backend.repository.TicketRepository;
import com.example.eventbook_backend.repository.UserRepository;
import com.example.eventbook_backend.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final TicketMapper ticketMapper;
    private final UserRepository userRepository;

    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public TicketResponse createTicket(Long bookingId) {

        User user = getCurrentUser();

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if(!booking.getUser().getId().equals(user.getId())){
            throw new IllegalStateException("You cannot create a ticket for this booking");
        }

        if(booking.getStatus() != BookingStatus.CONFIRMED){
            throw new IllegalStateException("Booking is not confirmed");
        }

        if(ticketRepository.existsByBookingId(bookingId)){
            throw new ResourceAlreadyExistsException("Ticket already exists");
        }

        String ticketNumber = "TICKET_"+ UUID.randomUUID().toString().replace("-","");

        Ticket ticket = Ticket.builder()
                .ticketNumber(ticketNumber)
                .booking(booking)
                .issuedAt(LocalDate.now())
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        List<BookingSeat> bookingSeats = bookingSeatRepository.findByBookingId(bookingId);

        return ticketMapper.toResponse(savedTicket, bookingSeats);
    }

    @Override
    @Transactional
    public TicketResponse getTicket(Long bookingId) {

        User user = getCurrentUser();

        Ticket ticket = ticketRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        if(!ticket.getBooking().getUser().getId().equals(user.getId())){
            throw new IllegalStateException("You cannot access this ticket");
        }

        List<BookingSeat> bookingSeats = bookingSeatRepository.findByBookingId(bookingId);

        return ticketMapper.toResponse(ticket,bookingSeats);
    }
}
