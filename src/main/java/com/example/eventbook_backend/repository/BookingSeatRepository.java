package com.example.eventbook_backend.repository;

import com.example.eventbook_backend.entity.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {

    List<BookingSeat> findByBookingId(Long bookingId);

    boolean existsByBookingIdAndShowSeatId(Long bookingId, Long showSeatId);

    List<BookingSeat> findByShowSeatId(Long showSeatId);
}
