package com.example.eventbook_backend.mapper;

import com.example.eventbook_backend.dto.movie.MovieResponse;
import com.example.eventbook_backend.entity.Movie;
import com.example.eventbook_backend.entity.Review;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieMapper {

    public MovieResponse toResponse(Movie movie){

        List<Review> reviews = movie.getReviews();
        List<String> directors = movie.getDirectors();

        double averageRating =reviews.isEmpty()
                ? 0.0
                : reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .duration(movie.getDuration())
                .language(movie.getLanguage())
                .releaseDate(movie.getReleaseDate())
                .posterUrl(movie.getPosterUrl())
                .trailerUrl(movie.getTrailerUrl())
                .certification(movie.getCertification())
                .status(movie.getStatus())
                .genres(movie.getGenres())
                .averageRating(averageRating)
                .reviewCount(reviews.size())
                .directors(directors)
                .createdAt(movie.getCreatedAt())
                .updatedAt(movie.getUpdatedAt())
                .build();
    }
}
