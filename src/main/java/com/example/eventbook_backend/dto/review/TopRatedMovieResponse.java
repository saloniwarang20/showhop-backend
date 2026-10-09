package com.example.eventbook_backend.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TopRatedMovieResponse {

    private Long movieId;
    private Double averageRating;
    private Long reviewCount;
}
