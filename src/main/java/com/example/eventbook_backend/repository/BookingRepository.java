package com.example.eventbook_backend.repository;

import com.example.eventbook_backend.entity.Booking;
import com.example.eventbook_backend.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingNumber(String bookingNumber);

    List<Booking> findByUserId(Long userId);

    List<Booking> findByStatus(BookingStatus status);

    boolean existsByUserIdAndMovieShowMovieIdAndStatus(
            Long userId,
            Long movieId,
            BookingStatus status
    );
}
