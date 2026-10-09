package com.example.eventbook_backend.service.impl;

import com.example.eventbook_backend.entity.Booking;
import com.example.eventbook_backend.entity.BookingSeat;
import com.example.eventbook_backend.entity.ShowSeat;
import com.example.eventbook_backend.enums.BookingStatus;
import com.example.eventbook_backend.enums.SeatStatus;
import com.example.eventbook_backend.exception.ResourceNotFoundException;
import com.example.eventbook_backend.repository.BookingRepository;
import com.example.eventbook_backend.repository.BookingSeatRepository;
import com.example.eventbook_backend.repository.ShowSeatRepository;
import com.example.eventbook_backend.service.SeatHoldService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatHoldServiceImpl implements SeatHoldService {

    private final ShowSeatRepository showSeatRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public void releaseExpiredSeats() {

        LocalDateTime currentTime = LocalDateTime.now();

        List<ShowSeat> showSeats = showSeatRepository.findByStatusAndHeldUntilLessThan(SeatStatus.HELD, currentTime);

        for(ShowSeat showSeat : showSeats){
            List<BookingSeat> bookingSeats = bookingSeatRepository.findByShowSeatId(showSeat.getId());

            for(BookingSeat bookingSeat : bookingSeats){
                Booking booking = bookingSeat.getBooking();

                if(booking.getStatus().equals(BookingStatus.PENDING)){
                    booking.setStatus(BookingStatus.EXPIRED);
                    bookingRepository.save(booking);
                }
            }

            showSeat.setStatus(SeatStatus.AVAILABLE);
            showSeat.setHeldUntil(null);

        }

        showSeatRepository.saveAll(showSeats);

    }
}
