package com.example.eventbook_backend.dto.movie;

import com.example.eventbook_backend.enums.MovieGenre;
import com.example.eventbook_backend.enums.MovieStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MovieSearchRequest {

    private String keyword;
    private MovieGenre genre;
    private String language;
    private String certification;
    private MovieStatus status;
    private String sortBy;
    private String direction;
}
