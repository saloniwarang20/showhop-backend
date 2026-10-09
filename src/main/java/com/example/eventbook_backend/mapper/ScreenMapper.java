package com.example.eventbook_backend.mapper;

import com.example.eventbook_backend.dto.screen.ScreenResponse;
import com.example.eventbook_backend.entity.Screen;
import org.springframework.stereotype.Component;

@Component
public class ScreenMapper {

    public ScreenResponse toResponse(Screen screen){
        return ScreenResponse.builder()
                .id(screen.getId())
                .name(screen.getName())
                .totalRows(screen.getTotalRows())
                .totalColumns(screen.getTotalColumns())
                .screenType(screen.getScreenType())
                .theatreId(screen.getTheatre().getId())
                .createdAt(screen.getCreatedAt())
                .updatedAt(screen.getUpdatedAt())
                .build();
    }
}
