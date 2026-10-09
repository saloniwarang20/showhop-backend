package com.example.eventbook_backend.dto.moviecast;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MovieCastRequest {

    @NotBlank(message = "Actor name is required")
    private String actorName;

    @NotBlank(message = "Character name is required")
    private String characterName;

    private String imageUrl;
}
