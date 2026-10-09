package com.example.eventbook_backend.service.impl;

import com.example.eventbook_backend.dto.booking.BookingRequest;
import com.example.eventbook_backend.dto.booking.BookingResponse;
import com.example.eventbook_backend.entity.*;
import com.example.eventbook_backend.enums.BookingStatus;
import com.example.eventbook_backend.enums.SeatStatus;
import com.example.eventbook_backend.mapper.BookingMapper;
import com.example.eventbook_backend.exception.ResourceNotFoundException;
import com.example.eventbook_backend.exception.IllegalStateException;
import com.example.eventbook_backend.repository.*;
import com.example.eventbook_backend.service.BookingService;
import com.example.eventbook_backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final ShowSeatRepository showSeatRepository;
    private final MovieShowRepository movieShowRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;
    private final PaymentService paymentService;

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    }

    private void releaseSeats(List<BookingSeat> bookingSeats) {

        for (BookingSeat bookingSeat : bookingSeats) {

            ShowSeat showSeat = bookingSeat.getShowSeat();

            showSeat.setStatus(SeatStatus.AVAILABLE);
            showSeat.setHeldUntil(null);
        }

        showSeatRepository.saveAll(
                bookingSeats.stream()
                        .map(BookingSeat::getShowSeat)
                        .toList()
        );
    }

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {

        //1. get logged-in user
        User user = getCurrentUser();

        //2. get movie show
        MovieShow movieShow = movieShowRepository.findById(request.getMovieShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie Show not found"));

        //3. get requested seats
        List<ShowSeat> showSeats = new ArrayList<>();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for(Long seatId : request.getShowSeatIds()){
            ShowSeat showSeat = showSeatRepository.findByMovieShowIdAndId(movieShow.getId(), seatId)
                    .orElseThrow(() -> new ResourceNotFoundException("Show Seat not found"));

            //4.check availability
            if(showSeat.getStatus() != SeatStatus.AVAILABLE){
                throw new IllegalStateException("Seat is not available");
            }

            showSeats.add(showSeat);

            //5. calculate total
            totalAmount = totalAmount.add(showSeat.getPrice());
        }

        //6. generate booking number
        String uniqueNumber = "SHOWHOP_"+UUID.randomUUID().toString().replace("-", "");

        //7. create booking
        Booking booking = Booking.builder()
                .bookingNumber(uniqueNumber)
                .user(user)
                .movieShow(movieShow)
                .totalAmount(totalAmount)
                .build();

        //8. save booking first
        Booking savedBooking = bookingRepository.save(booking);

        //9. create booking seats records
        List<BookingSeat> bookingSeats = new ArrayList<>();

        LocalDateTime heldUntil = LocalDateTime.now().plusMinutes(10);

        for(ShowSeat showSeat : showSeats){
            BookingSeat bookingSeat = BookingSeat.builder()
                    .booking(savedBooking)
                    .showSeat(showSeat)
                    .price(showSeat.getPrice())
                    .build();

            bookingSeats.add(bookingSeat);

            //10. mark seat as HELD
            showSeat.setStatus(SeatStatus.HELD);

            showSeat.setHeldUntil(heldUntil);
        }

        //11. save booking seats
        List<BookingSeat> savedBookingSeats = bookingSeatRepository.saveAll(bookingSeats);

        //12. save updated show seats
        showSeatRepository.saveAll(showSeats);

        paymentService.createPayment(savedBooking.getId());

        //13. return response
        return bookingMapper.toResponse(savedBooking,savedBookingSeats);
    }

    @Override
    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        return bookingMapper.toResponse(booking, bookingSeatRepository.findByBookingId(id));
    }

    @Override
    public BookingResponse getBookingByBookingNumber(String bookingNumber) {

        Booking booking = bookingRepository.findByBookingNumber(bookingNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        return bookingMapper.toResponse(booking, bookingSeatRepository.findByBookingId(booking.getId()));
    }

    @Override
    public List<BookingResponse> getMyBookings() {

        User user = getCurrentUser();

        return bookingRepository.findByUserId(user.getId())
                .stream()
                .map(booking -> bookingMapper.toResponse(booking, bookingSeatRepository.findByBookingId(booking.getId())))
                .toList();
    }

    @Override
    @Transactional
    public void cancelBooking(Long id) {
        User user = getCurrentUser();

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        //make sure user can cancel their own booking
        if(!booking.getUser().getId().equals(user.getId())){
            throw new IllegalStateException("You cannot cancel this booking");
        }

        if(booking.getStatus() == BookingStatus.CANCELLED){
            throw new IllegalStateException("Booking is already cancelled");
        }

        //get booking seats
        List<BookingSeat> bookingSeats = bookingSeatRepository.findByBookingId(id);

        //release seats
        releaseSeats(bookingSeats);

        //cancel booking
        booking.setStatus(BookingStatus.CANCELLED);

        bookingRepository.save(booking);
    }

    @Override
    @Transactional
    @Scheduled(fixedRate = 60000)
    public void expireBookings() {
        List<Booking> pendingBookings = bookingRepository.findByStatus(BookingStatus.PENDING);

        LocalDateTime now = LocalDateTime.now();

        for(Booking booking: pendingBookings){
            List<BookingSeat> bookingSeats = bookingSeatRepository.findByBookingId(booking.getId());

            boolean expired = false;

            for(BookingSeat bookingSeat: bookingSeats){
                ShowSeat showSeat = bookingSeat.getShowSeat();

                if(showSeat.getHeldUntil() != null && !showSeat.getHeldUntil().isAfter(now)){
                    expired = true;
                    break;
                }
            }

            if(expired){

                releaseSeats(bookingSeats);

                //cancel booking
                booking.setStatus(BookingStatus.CANCELLED);

                bookingRepository.save(booking);

            }
        }
    }
}
