package com.example.eventbook_backend.service;

import com.example.eventbook_backend.dto.seat.SeatRequest;
import com.example.eventbook_backend.dto.seat.SeatResponse;

import java.util.List;

public interface SeatService {

    SeatResponse createSeat(SeatRequest request);

    SeatResponse getSeatById(Long id);

    List<SeatResponse> getAllSeats();

    List<SeatResponse> getSeatsByScreen(Long screenId);

    SeatResponse updateSeat(Long id, SeatRequest request);

    void deleteSeat(Long id);
}
