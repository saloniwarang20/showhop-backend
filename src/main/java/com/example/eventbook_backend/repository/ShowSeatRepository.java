package com.example.eventbook_backend.repository;

import com.example.eventbook_backend.entity.ShowSeat;
import com.example.eventbook_backend.enums.SeatStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {

    List<ShowSeat> findByMovieShowId(Long movieShowId);

    List<ShowSeat> findByMovieShowIdAndStatus(Long movieShowId, SeatStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ShowSeat> findByMovieShowIdAndId(Long movieShowId, Long id);

    boolean existsByMovieShowIdAndSeatId(Long movieShowId, Long seatId);

    List<ShowSeat> findByStatusAndHeldUntilLessThan(SeatStatus status, LocalDateTime heldUntil);
}
