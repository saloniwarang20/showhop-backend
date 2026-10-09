package com.example.eventbook_backend.service.impl;

import com.example.eventbook_backend.dto.movieShow.MovieShowRequest;
import com.example.eventbook_backend.dto.movieShow.MovieShowResponse;
import com.example.eventbook_backend.dto.showSeat.ShowSeatResponse;
import com.example.eventbook_backend.entity.*;
import com.example.eventbook_backend.enums.SeatStatus;
import com.example.eventbook_backend.exception.ResourceAlreadyExistsException;
import com.example.eventbook_backend.exception.ResourceNotFoundException;
import com.example.eventbook_backend.mapper.MovieShowMapper;
import com.example.eventbook_backend.mapper.ShowSeatMapper;
import com.example.eventbook_backend.repository.*;
import com.example.eventbook_backend.service.MovieShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieShowServiceImpl implements MovieShowService {

    private final MovieShowRepository movieShowRepository;
    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;
    private final MovieShowMapper movieShowMapper;
    private final SeatRepository seatRepository;
    private final ShowSeatRepository showSeatRepository;
    private final ShowSeatMapper showSeatMapper;

    @Override
    @Transactional
    public MovieShowResponse createShow(MovieShowRequest request) {

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        Screen screen = screenRepository.findById(request.getScreenId())
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found"));

        if(!request.getEndTime().isAfter(request.getStartTime())){
            throw new IllegalStateException("End Time must be after Start Time");
        }

        if(movieShowRepository.existsOverlappingShow(request.getScreenId(), request.getShowDate(), request.getStartTime(), request.getEndTime())){
            throw new ResourceAlreadyExistsException("There is already a show running at this  time");
        }

        //1. create the movie show
        MovieShow movieShow = MovieShow.builder()
                .movie(movie)
                .screen(screen)
                .showDate(request.getShowDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .basePrice(request.getBasePrice())
                .build();

        MovieShow savedShow = movieShowRepository.save(movieShow);

        //2. get all physical seats of this screen
        List<Seat> seats = seatRepository.findByScreenId(screen.getId());

        //3. create show seat for every physical seat
        List<ShowSeat> showSeats = seats.stream()
                .map(seat -> ShowSeat.builder()
                        .movieShow(movieShow)
                        .seat(seat)
                        .price(request.getBasePrice().add(seat.getPrice()))
                        .status(SeatStatus.AVAILABLE)
                        .build()
                )
                .toList();

        //4. save all the show seats
        showSeatRepository.saveAll(showSeats);

        return movieShowMapper.toResponse(savedShow);
    }

    @Override
    public MovieShowResponse getShowById(Long id) {

        MovieShow show = movieShowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie Show not found"));

        return movieShowMapper.toResponse(show);
    }

    @Override
    public List<MovieShowResponse> getAllShows() {
        return movieShowRepository.findAll()
                .stream()
                .map(movieShowMapper::toResponse)
                .toList();
    }

    @Override
    public List<MovieShowResponse> getShowsByMovie(Long movieId) {

        movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        return movieShowRepository.findByMovieId(movieId)
                .stream()
                .map(movieShowMapper::toResponse)
                .toList();
    }

    @Override
    public List<MovieShowResponse> getShowsByScreen(Long screenId) {

        screenRepository.findById(screenId)
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found"));

        return movieShowRepository.findByScreenId(screenId)
                .stream()
                .map(movieShowMapper::toResponse)
                .toList();
    }

    @Override
    public List<MovieShowResponse> getShowsByMovieAndDate(Long movieId, LocalDate date) {

        movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        return movieShowRepository.findByMovieIdAndShowDate(movieId, date)
                .stream()
                .map(movieShowMapper::toResponse)
                .toList();
    }

    @Override
    public MovieShowResponse updateShow(Long id, MovieShowRequest request) {

        MovieShow show = movieShowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie Show not found"));

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        Screen screen = screenRepository.findById(request.getScreenId())
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found"));

        if(!request.getEndTime().isAfter(request.getStartTime())){
            throw new IllegalArgumentException("End Time must be after Start Time");
        }

        if(movieShowRepository.existsOverlappingShowForUpdate(id, request.getScreenId(), request.getShowDate(), request.getStartTime(), request.getEndTime())){
            throw new ResourceAlreadyExistsException("There is already a show running at this  time");
        }

        show.setMovie(movie);
        show.setScreen(screen);
        show.setShowDate(request.getShowDate());
        show.setStartTime(request.getStartTime());
        show.setEndTime(request.getEndTime());
        show.setBasePrice(request.getBasePrice());

        MovieShow updatedShow = movieShowRepository.save(show);

        return movieShowMapper.toResponse(updatedShow);
    }

    @Override
    public void deleteShow(Long id) {
        MovieShow show = movieShowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie Show not found"));

        movieShowRepository.delete(show);

    }

    @Override
    public List<ShowSeatResponse> getSeatsForShow(Long movieShowId) {

        movieShowRepository.findById(movieShowId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie Show not found"));

        return showSeatRepository.findByMovieShowId(movieShowId)
                .stream()
                .map(showSeatMapper::toResponse)
                .toList();
    }
}
