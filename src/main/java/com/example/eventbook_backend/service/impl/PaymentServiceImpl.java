package com.example.eventbook_backend.service.impl;

import com.example.eventbook_backend.dto.payment.PaymentResponse;
import com.example.eventbook_backend.entity.*;
import com.example.eventbook_backend.enums.BookingStatus;
import com.example.eventbook_backend.enums.PaymentStatus;
import com.example.eventbook_backend.enums.SeatStatus;
import com.example.eventbook_backend.exception.IllegalStateException;
import com.example.eventbook_backend.exception.ResourceAlreadyExistsException;
import com.example.eventbook_backend.exception.ResourceNotFoundException;
import com.example.eventbook_backend.mapper.PaymentMapper;
import com.example.eventbook_backend.repository.BookingRepository;
import com.example.eventbook_backend.repository.BookingSeatRepository;
import com.example.eventbook_backend.repository.PaymentRepository;
import com.example.eventbook_backend.repository.UserRepository;
import com.example.eventbook_backend.service.PaymentService;
import com.example.eventbook_backend.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final UserRepository userRepository;
    private final PaymentMapper paymentMapper;
    private final TicketService ticketService;

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
    public PaymentResponse createPayment(Long bookingId) {

        User user = getCurrentUser();

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if(!booking.getUser().getId().equals(user.getId())){
            throw new IllegalStateException("You cannot pay for this booking.");
        }

        if(booking.getStatus() != BookingStatus.PENDING){
            throw new IllegalStateException("Booking has expired");
        }

        if(paymentRepository.existsByBookingId(bookingId)){
            throw new ResourceAlreadyExistsException("Payment already exists");
        }

        List<BookingSeat> bookingSeats = bookingSeatRepository.findByBookingId(bookingId);

        for(BookingSeat bookingSeat : bookingSeats){
            ShowSeat showSeat = bookingSeat.getShowSeat();

            if(showSeat.getStatus() != SeatStatus.HELD){
                throw new IllegalStateException("Seat is no longer held");
            }

            if(showSeat.getHeldUntil() == null || showSeat.getHeldUntil().isBefore(LocalDateTime.now())){
                throw  new IllegalStateException("Seat hold has expired");
            }
        }

        String paymentNumber = "PAY_"+UUID.randomUUID().toString().replace("-","");

        Payment payment = Payment.builder()
                .paymentNumber(paymentNumber)
                .booking(booking)
                .amount(booking.getTotalAmount())
                .status(PaymentStatus.PENDING)
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        return paymentMapper.toResponse(savedPayment);
    }

    @Override
    @Transactional
    public PaymentResponse completePayment(Long bookingId) {

        User user = getCurrentUser();

        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if(!payment.getBooking().getUser().getId().equals(user.getId())){
            throw new IllegalStateException("You cannot complete this payment");
        }

        if(payment.getStatus() != PaymentStatus.PENDING){
            throw new IllegalStateException("Payment is not pending");
        }

        Booking booking = payment.getBooking();

        if(booking.getStatus() != BookingStatus.PENDING){
            throw new IllegalStateException("Booking has expired");
        }

        List<BookingSeat> bookingSeats = bookingSeatRepository.findByBookingId(bookingId);

        for(BookingSeat bookingSeat: bookingSeats){
            ShowSeat seat = bookingSeat.getShowSeat();
            if(seat.getStatus() != SeatStatus.HELD || seat.getHeldUntil() == null ||seat.getHeldUntil().isBefore(LocalDateTime.now())){
                throw new IllegalStateException("Seat hold has expired or seat is not held");
            }
        }

        for(BookingSeat bookingSeat :  bookingSeats){
            ShowSeat seat = bookingSeat.getShowSeat();

            seat.setStatus(SeatStatus.BOOKED);
            seat.setHeldUntil(null);
        }

        payment.setStatus(PaymentStatus.SUCCESS);
        booking.setStatus(BookingStatus.CONFIRMED);

        Payment savedPayment = paymentRepository.save(payment);
        bookingRepository.save(booking);

        ticketService.createTicket(bookingId);

        return paymentMapper.toResponse(savedPayment);
    }
}
