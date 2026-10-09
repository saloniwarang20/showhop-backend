package com.example.eventbook_backend.dto.movie;

import com.example.eventbook_backend.enums.MovieGenre;
import com.example.eventbook_backend.enums.MovieStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Builder
@Setter
@Getter
public class MovieResponse {

    private Long id;
    private String title;
    private String description;
    private Integer duration;
    private String language;
    private LocalDate releaseDate;
    private String posterUrl;
    private String trailerUrl;
    private String certification;
    private MovieStatus status;
    private Double averageRating;
    private Integer reviewCount;
    private List<String> directors;
    private Set<MovieGenre> genres;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
