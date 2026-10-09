package com.example.eventbook_backend.mapper;

import com.example.eventbook_backend.dto.movieShow.MovieShowResponse;
import com.example.eventbook_backend.entity.MovieShow;
import org.springframework.stereotype.Component;

@Component
public class MovieShowMapper {

    public MovieShowResponse toResponse(MovieShow movieShow){
        return MovieShowResponse.builder()
                .id(movieShow.getId())
                .movieId(movieShow.getMovie().getId())
                .movieTitle(movieShow.getMovie().getTitle())
                .screenId(movieShow.getScreen().getId())
                .screenName(movieShow.getScreen().getName())
                .theatreId(movieShow.getScreen().getTheatre().getId())
                .theatreName(movieShow.getScreen().getTheatre().getName())
                .showDate(movieShow.getShowDate())
                .startTime(movieShow.getStartTime())
                .endTime(movieShow.getEndTime())
                .basePrice(movieShow.getBasePrice())
                .showStatus(movieShow.getShowStatus())
                .createdAt(movieShow.getCreatedAt())
                .updatedAt(movieShow.getUpdatedAt())
                .build();
    }
}
