package com.example.eventbook_backend.repository;

import com.example.eventbook_backend.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByScreenId(Long screenId);

    boolean existsByScreenIdAndSeatNumber(Long screenId, String seatNumber);

    boolean existsByScreenIdAndSeatNumberAndIdNot(Long screenId, String seatNumber, Long id);

}
