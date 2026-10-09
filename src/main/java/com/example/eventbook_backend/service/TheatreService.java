package com.example.eventbook_backend.service;

import com.example.eventbook_backend.dto.theatre.TheatreRequest;
import com.example.eventbook_backend.dto.theatre.TheatreResponse;

import java.util.List;

public interface TheatreService {

    TheatreResponse createTheatre(TheatreRequest request);

    TheatreResponse getTheatreById(Long id);

    List<TheatreResponse> getAllTheatres();

    TheatreResponse updateTheatre(Long id, TheatreRequest request);

    void deleteTheatre(Long id);
}
