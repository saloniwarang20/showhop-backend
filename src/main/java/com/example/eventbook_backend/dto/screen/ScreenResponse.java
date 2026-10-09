package com.example.eventbook_backend.dto.screen;

import com.example.eventbook_backend.enums.ScreenType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class ScreenResponse {

    private Long id;
    private String name;
    private Integer totalRows;
    private Integer totalColumns;
    private ScreenType screenType;
    private Long theatreId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
