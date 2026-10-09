package com.example.eventbook_backend.service.impl;

import com.example.eventbook_backend.dto.seat.SeatRequest;
import com.example.eventbook_backend.dto.seat.SeatResponse;
import com.example.eventbook_backend.entity.Screen;
import com.example.eventbook_backend.entity.Seat;
import com.example.eventbook_backend.exception.ResourceAlreadyExistsException;
import com.example.eventbook_backend.exception.ResourceNotFoundException;
import com.example.eventbook_backend.mapper.SeatMapper;
import com.example.eventbook_backend.repository.ScreenRepository;
import com.example.eventbook_backend.repository.SeatRepository;
import com.example.eventbook_backend.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final ScreenRepository screenRepository;
    private final SeatMapper seatMapper;

    @Override
    public SeatResponse createSeat(SeatRequest request) {

        Screen screen = screenRepository.findById(request.getScreenId())
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found"));

        if(seatRepository.existsByScreenIdAndSeatNumber(request.getScreenId(), request.getSeatNumber())){
            throw new ResourceAlreadyExistsException("Seat already exists in screen");
        }

        Seat seat = Seat.builder()
                .seatNumber(request.getSeatNumber())
                .rowNumber(request.getRowNumber())
                .seatType(request.getSeatType())
                .price(request.getPrice())
                .screen(screen)
                .build();

        Seat savedSeat = seatRepository.save(seat);

        return seatMapper.toResponse(savedSeat);
    }

    @Override
    public SeatResponse getSeatById(Long id) {

        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));

        return seatMapper.toResponse(seat);
    }

    @Override
    public List<SeatResponse> getAllSeats() {
        return seatRepository.findAll()
                .stream()
                .map(seatMapper::toResponse)
                .toList();
    }

    @Override
    public List<SeatResponse> getSeatsByScreen(Long screenId) {
        screenRepository.findById(screenId)
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found"));

        return seatRepository.findByScreenId(screenId)
                .stream()
                .map(seatMapper::toResponse)
                .toList();
    }

    @Override
    public SeatResponse updateSeat(Long id, SeatRequest request) {

        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));

        Screen screen = screenRepository.findById(request.getScreenId())
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found"));

        if(seatRepository.existsByScreenIdAndSeatNumberAndIdNot(request.getScreenId(), request.getSeatNumber(), id)){
            throw new ResourceAlreadyExistsException("Seat already exists in screen");
        }

        seat.setSeatNumber(request.getSeatNumber());
        seat.setRowNumber(request.getRowNumber());
        seat.setSeatType(request.getSeatType());
        seat.setPrice(request.getPrice());
        seat.setScreen(screen);

        Seat updatedSeat = seatRepository.save(seat);

        return seatMapper.toResponse(updatedSeat);
    }

    @Override
    public void deleteSeat(Long id) {

        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));

        seatRepository.delete(seat);

    }
}
