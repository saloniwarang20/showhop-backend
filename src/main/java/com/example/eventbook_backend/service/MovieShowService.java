package com.example.eventbook_backend.service;

import com.example.eventbook_backend.dto.movieShow.MovieShowRequest;
import com.example.eventbook_backend.dto.movieShow.MovieShowResponse;
import com.example.eventbook_backend.dto.showSeat.ShowSeatResponse;

import java.time.LocalDate;
import java.util.List;

public interface MovieShowService {

    MovieShowResponse createShow(MovieShowRequest request);

    MovieShowResponse getShowById(Long id);

    List<MovieShowResponse> getAllShows();

    List<MovieShowResponse> getShowsByMovie(Long movieId);

    List<MovieShowResponse> getShowsByScreen(Long screenId);

    List<MovieShowResponse> getShowsByMovieAndDate(Long movieId, LocalDate date);

    MovieShowResponse updateShow(Long id, MovieShowRequest request);

    void deleteShow(Long id);

    List<ShowSeatResponse> getSeatsForShow(Long movieShowId);
}
