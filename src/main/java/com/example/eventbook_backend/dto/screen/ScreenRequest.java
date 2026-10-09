package com.example.eventbook_backend.dto.screen;

import com.example.eventbook_backend.enums.ScreenType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ScreenRequest {

    private String name;
    private Integer totalRows;
    private Integer totalColumns;
    private ScreenType screenType;
    private Long theatreId;

}
