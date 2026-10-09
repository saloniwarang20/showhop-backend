package com.example.eventbook_backend.service;

import com.example.eventbook_backend.dto.moviecast.MovieCastRequest;
import com.example.eventbook_backend.dto.moviecast.MovieCastResponse;

import java.util.List;

public interface MovieCastService {

    MovieCastResponse addCast(Long movieId,MovieCastRequest request);

    List<MovieCastResponse> getMovieCast(Long movieId);

    MovieCastResponse updateCast(Long castId, MovieCastRequest request);

    void deleteCast(Long castId);
}
