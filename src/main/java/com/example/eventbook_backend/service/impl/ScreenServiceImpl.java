package com.example.eventbook_backend.service.impl;

import com.example.eventbook_backend.dto.screen.ScreenRequest;
import com.example.eventbook_backend.dto.screen.ScreenResponse;
import com.example.eventbook_backend.entity.Screen;
import com.example.eventbook_backend.entity.Seat;
import com.example.eventbook_backend.entity.Theatre;
import com.example.eventbook_backend.enums.SeatType;
import com.example.eventbook_backend.exception.ResourceAlreadyExistsException;
import com.example.eventbook_backend.exception.ResourceNotFoundException;
import com.example.eventbook_backend.mapper.ScreenMapper;
import com.example.eventbook_backend.repository.ScreenRepository;
import com.example.eventbook_backend.repository.SeatRepository;
import com.example.eventbook_backend.repository.TheatreRepository;
import com.example.eventbook_backend.service.ScreenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreenServiceImpl implements ScreenService {

    private final ScreenRepository screenRepository;
    private final TheatreRepository theatreRepository;
    private final SeatRepository seatRepository;
    private final ScreenMapper screenMapper;

    @Override
    public ScreenResponse createScreen(ScreenRequest request) {

        Theatre theatre = theatreRepository.findById(request.getTheatreId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Theatre not found"));

        if (screenRepository.existsByTheatreIdAndName(
                request.getTheatreId(),
                request.getName())) {

            throw new ResourceAlreadyExistsException(
                    "Screen already exists in Theatre");
        }

        Screen screen = Screen.builder()
                .name(request.getName())
                .totalRows(request.getTotalRows())
                .totalColumns(request.getTotalColumns())
                .screenType(request.getScreenType())
                .theatre(theatre)
                .build();

        Screen savedScreen = screenRepository.save(screen);

        // Automatically create seats
        createSeatsForScreen(savedScreen);

        return screenMapper.toResponse(savedScreen);
    }

    private void createSeatsForScreen(Screen screen) {

        int totalRows = screen.getTotalRows();
        int totalColumns = screen.getTotalColumns();

        for (int row = 0; row < totalRows; row++) {

            String rowNumber =
                    String.valueOf((char) ('A' + row));

            for (int column = 1; column <= totalColumns; column++) {

                SeatType seatType;
                BigDecimal price;

                /*
                 * Last row = Recliner
                 */
                if (row == totalRows - 1) {

                    seatType = SeatType.RECLINER;
                    price = BigDecimal.valueOf(350);

                    /*
                     * Last 3 rows before recliner = Premium
                     */
                } else if (row >= totalRows - 3) {

                    seatType = SeatType.PREMIUM;
                    price = BigDecimal.valueOf(250);

                    /*
                     * Remaining rows = Regular
                     */
                } else {

                    seatType = SeatType.REGULAR;
                    price = BigDecimal.valueOf(200);
                }

                Seat seat = Seat.builder()
                        .seatNumber(rowNumber + column)
                        .rowNumber(rowNumber)
                        .seatType(seatType)
                        .price(price)
                        .screen(screen)
                        .build();

                seatRepository.save(seat);
            }
        }
    }

    @Override
    public ScreenResponse getScreenById(Long id) {

        Screen screen = screenRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Screen not found"));

        return screenMapper.toResponse(screen);
    }

    @Override
    public List<ScreenResponse> getAllScreens() {

        return screenRepository.findAll()
                .stream()
                .map(screenMapper::toResponse)
                .toList();
    }

    @Override
    public List<ScreenResponse> getScreensByTheatre(Long theatreId) {

        theatreRepository.findById(theatreId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Theatre not found"));

        return screenRepository.findByTheatreId(theatreId)
                .stream()
                .map(screenMapper::toResponse)
                .toList();
    }

    @Override
    public ScreenResponse updateScreen(
            Long id,
            ScreenRequest request) {

        Screen screen = screenRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Screen not found"));

        Theatre theatre = theatreRepository.findById(
                        request.getTheatreId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Theatre not found"));

        if (screenRepository.existsByTheatreIdAndNameAndIdNot(
                request.getTheatreId(),
                request.getName(),
                id)) {

            throw new ResourceAlreadyExistsException(
                    "Screen already exists in Theatre");
        }

        screen.setName(request.getName());
        screen.setTotalRows(request.getTotalRows());
        screen.setTotalColumns(request.getTotalColumns());
        screen.setScreenType(request.getScreenType());
        screen.setTheatre(theatre);

        Screen updatedScreen = screenRepository.save(screen);

        return screenMapper.toResponse(updatedScreen);
    }

    @Override
    public void deleteScreen(Long id) {

        Screen screen = screenRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Screen not found"));

        screenRepository.delete(screen);
    }
}