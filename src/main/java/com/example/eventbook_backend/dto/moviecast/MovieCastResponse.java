package com.example.eventbook_backend.dto.moviecast;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class MovieCastResponse {

    private Long id;
    private Long movieId;
    private String actorName;
    private String characterName;
    private String imageUrl;
}
