package com.example.eventbook_backend.service.impl;

import com.example.eventbook_backend.dto.moviecast.MovieCastRequest;
import com.example.eventbook_backend.dto.moviecast.MovieCastResponse;
import com.example.eventbook_backend.entity.Movie;
import com.example.eventbook_backend.entity.MovieCast;
import com.example.eventbook_backend.exception.ResourceNotFoundException;
import com.example.eventbook_backend.mapper.MovieCastMapper;
import com.example.eventbook_backend.repository.MovieCastRepository;
import com.example.eventbook_backend.repository.MovieRepository;
import com.example.eventbook_backend.service.MovieCastService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieCastServiceImpl implements MovieCastService {

    private final MovieCastRepository movieCastRepository;
    private final MovieRepository movieRepository;
    private final MovieCastMapper movieCastMapper;

    @Override
    @Transactional
    public MovieCastResponse addCast(Long movieId, MovieCastRequest request) {

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        MovieCast movieCast = MovieCast.builder()
                .movie(movie)
                .actorName(request.getActorName())
                .characterName(request.getCharacterName())
                .imageUrl(request.getImageUrl())
                .build();

        MovieCast savedMovieCast = movieCastRepository.save(movieCast);

        return movieCastMapper.toResponse(savedMovieCast);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieCastResponse> getMovieCast(Long movieId) {

        movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        return movieCastRepository.findByMovieId(movieId)
                .stream()
                .map(movieCastMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public MovieCastResponse updateCast(Long castId, MovieCastRequest request) {

        MovieCast movieCast = movieCastRepository.findById(castId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie Cast not found"));

        movieCast.setActorName(request.getActorName());
        movieCast.setCharacterName(request.getCharacterName());
        movieCast.setImageUrl(request.getImageUrl());

        MovieCast updatedCast = movieCastRepository.save(movieCast);

        return movieCastMapper.toResponse(updatedCast);
    }

    @Override
    @Transactional
    public void deleteCast(Long castId) {
        MovieCast movieCast = movieCastRepository.findById(castId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie Cast not found"));

        movieCastRepository.delete(movieCast);
    }
}
