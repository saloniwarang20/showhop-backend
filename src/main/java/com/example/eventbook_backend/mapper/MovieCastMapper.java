package com.example.eventbook_backend.mapper;

import com.example.eventbook_backend.dto.moviecast.MovieCastResponse;
import com.example.eventbook_backend.entity.MovieCast;
import org.springframework.stereotype.Component;

@Component
public class MovieCastMapper {

    public MovieCastResponse toResponse(MovieCast movieCast){
        return MovieCastResponse.builder()
                .id(movieCast.getId())
                .movieId(movieCast.getMovie().getId())
                .actorName(movieCast.getActorName())
                .characterName(movieCast.getCharacterName())
                .imageUrl(movieCast.getImageUrl())
                .build();
    }
}
