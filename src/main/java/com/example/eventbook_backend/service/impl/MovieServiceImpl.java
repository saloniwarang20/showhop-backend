package com.example.eventbook_backend.service.impl;

import com.example.eventbook_backend.dto.movie.MovieRequest;
import com.example.eventbook_backend.dto.movie.MovieResponse;
import com.example.eventbook_backend.entity.Movie;
import com.example.eventbook_backend.enums.MovieGenre;
import com.example.eventbook_backend.enums.MovieStatus;
import com.example.eventbook_backend.exception.ResourceNotFoundException;
import com.example.eventbook_backend.exception.IllegalStateException;
import com.example.eventbook_backend.mapper.MovieMapper;
import com.example.eventbook_backend.repository.MovieRepository;
import com.example.eventbook_backend.repository.ReviewRepository;
import com.example.eventbook_backend.service.MovieService;
import com.example.eventbook_backend.specification.MovieSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final ReviewRepository reviewRepository;

    @Override
    public MovieResponse createMovie(MovieRequest request) {

        if(movieRepository.existsByTitle(request.getTitle())){
            throw new IllegalStateException("Movie Title already exists");
        }

        Movie movie = Movie.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .duration(request.getDuration())
                .language(request.getLanguage())
                .releaseDate(request.getReleaseDate())
                .posterUrl(request.getPosterUrl())
                .trailerUrl(request.getTrailerUrl())
                .directors(request.getDirectors())
                .certification(request.getCertification())
                .build();

        Movie savedMovie = movieRepository.save(movie);

        return movieMapper.toResponse(savedMovie);
    }

    @Override
    public MovieResponse getMovieById(Long id) {

        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        return movieMapper.toResponse(movie);
    }

    @Override
    public List<MovieResponse> getAllMovies() {
        return movieRepository.findAll()
                .stream()
                .map(movieMapper::toResponse)
                .toList();
    }

    @Override
    public MovieResponse updateMovie(Long id, MovieRequest request) {

        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        if(movieRepository.existsByTitleAndIdNot(request.getTitle(), id)){
            throw new IllegalStateException("Movie Title already exists");
        }

        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setDuration(request.getDuration());
        movie.setLanguage(request.getLanguage());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setPosterUrl(request.getPosterUrl());
        movie.setTrailerUrl(request.getTrailerUrl());
        movie.setCertification(request.getCertification());
        movie.setStatus(request.getStatus());
        movie.setGenres(request.getGenres());
        movie.setDirectors(request.getDirectors());

        Movie updatedMovie = movieRepository.save(movie);

        return movieMapper.toResponse(updatedMovie);
    }

    @Override
    public void deleteMovie(Long id) {

        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        movieRepository.delete(movie);

    }

    @Override
    public Page<MovieResponse> searchMovies(String keyword, MovieGenre genre, String language, String certification, MovieStatus status, Pageable pageable) {

        Specification<Movie> specification = Specification
                .where(MovieSpecification.hasKeyword(keyword))
                .and(MovieSpecification.hasGenre(genre))
                .and(MovieSpecification.hasLanguage(language))
                .and(MovieSpecification.hasCertification(certification))
                .and(MovieSpecification.hasStatus(status));

        Page<Movie> movies = movieRepository.findAll(specification,pageable);

        return movies
                .map(movieMapper::toResponse);
    }


}
