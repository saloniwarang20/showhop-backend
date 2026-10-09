package com.example.eventbook_backend.dto.review;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
public class ReviewResponse {

    private long id;
    private String username;
    private Long movieId;
    private String movieTitle;
    private Integer rating;
    private String comment;
    private LocalDateTime date;

}
