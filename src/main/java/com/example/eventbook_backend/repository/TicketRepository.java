package com.example.eventbook_backend.repository;

import com.example.eventbook_backend.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByBookingId(Long bookingId);

    boolean existsByBookingId(Long bookingId);
}
