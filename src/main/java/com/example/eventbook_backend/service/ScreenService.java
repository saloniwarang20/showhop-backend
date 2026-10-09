package com.example.eventbook_backend.service;

import com.example.eventbook_backend.dto.screen.ScreenRequest;
import com.example.eventbook_backend.dto.screen.ScreenResponse;

import java.util.List;

public interface ScreenService {

    ScreenResponse createScreen(ScreenRequest request);

    ScreenResponse getScreenById(Long id);

    List<ScreenResponse> getAllScreens();

    List<ScreenResponse> getScreensByTheatre(Long theatreId);

    ScreenResponse updateScreen(Long id, ScreenRequest request);

    void deleteScreen(Long id);
}
