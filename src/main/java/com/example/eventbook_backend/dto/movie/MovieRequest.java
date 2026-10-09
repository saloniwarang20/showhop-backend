package com.example.eventbook_backend.dto.movie;

import com.example.eventbook_backend.enums.MovieGenre;
import com.example.eventbook_backend.enums.MovieStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Setter
@Getter
public class MovieRequest {

    private String title;
    private String description;
    private Integer duration;
    private String language;
    private LocalDate releaseDate;
    private String posterUrl;
    private String trailerUrl;
    private List<String> directors;
    private Set<MovieGenre> genres;
    private String certification;
    private MovieStatus status;
}
