package com.example.eventbook_backend.service;

import com.example.eventbook_backend.dto.movie.MovieRequest;
import com.example.eventbook_backend.dto.movie.MovieResponse;
import com.example.eventbook_backend.enums.MovieGenre;
import com.example.eventbook_backend.enums.MovieStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieService {

    MovieResponse createMovie(MovieRequest request);

    MovieResponse getMovieById(Long id);

    List<MovieResponse> getAllMovies();

    MovieResponse updateMovie(Long id, MovieRequest request);

    void deleteMovie(Long id);

    Page<MovieResponse> searchMovies(
            String keyword,
            MovieGenre genre,
            String language,
            String certification,
            MovieStatus status,
            Pageable pageable
    );
}
