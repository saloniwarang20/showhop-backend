package com.example.eventbook_backend.mapper;

import com.example.eventbook_backend.dto.ticket.TicketResponse;
import com.example.eventbook_backend.entity.BookingSeat;
import com.example.eventbook_backend.entity.Ticket;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class TicketMapper {

    public TicketResponse toResponse(Ticket ticket, List<BookingSeat> bookingSeats){
        String movieTitle = ticket.getBooking().getMovieShow().getMovie().getTitle();
        String theatreName = ticket.getBooking().getMovieShow().getScreen().getTheatre().getName();
        LocalDate showDate = ticket.getBooking().getMovieShow().getShowDate();
        LocalTime showTime = ticket.getBooking().getMovieShow().getStartTime();
        List<String> seats = bookingSeats.stream()
                .map(bookingSeat -> bookingSeat.getShowSeat().getSeat().getSeatNumber())
                .toList();

        return TicketResponse.builder()
                .id(ticket.getId())
                .ticketNumber(ticket.getTicketNumber())
                .bookingNumber(ticket.getBooking().getBookingNumber())
                .movieTitle(movieTitle)
                .theatreName(theatreName)
                .showDate(showDate)
                .showTime(showTime)
                .seats(seats)
                .amount(ticket.getBooking().getTotalAmount())
                .build();
    }
}
