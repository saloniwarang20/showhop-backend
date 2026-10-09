package com.example.eventbook_backend.dto.movieShow;

import com.example.eventbook_backend.enums.ShowStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Setter
@Getter
@Builder
public class MovieShowResponse {

    private Long id;
    private Long movieId;
    private String movieTitle;
    private Long screenId;
    private String screenName;
    private Long theatreId;
    private String theatreName;
    private LocalDate showDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private BigDecimal basePrice;
    private ShowStatus showStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
