package com.example.eventbook_backend.controller;

import com.example.eventbook_backend.dto.screen.ScreenRequest;
import com.example.eventbook_backend.dto.screen.ScreenResponse;
import com.example.eventbook_backend.service.ScreenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/screens")
@RequiredArgsConstructor
public class ScreenController {

    private final ScreenService screenService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ScreenResponse> createScreen(@Valid @RequestBody ScreenRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(screenService.createScreen(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScreenResponse> getScreenById(@PathVariable Long id){
        return ResponseEntity.ok(screenService.getScreenById(id));
    }

    @GetMapping
    public ResponseEntity<List<ScreenResponse>> getAllScreens(){
        return ResponseEntity.ok(screenService.getAllScreens());
    }

    @GetMapping("/theatre/{theatreId}")
    public ResponseEntity<List<ScreenResponse>> getScreensByTheatre(@PathVariable Long theatreId){
        return ResponseEntity.ok(screenService.getScreensByTheatre(theatreId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ScreenResponse> updateScreen(@PathVariable Long id, @Valid @RequestBody ScreenRequest request){
        return ResponseEntity.ok(screenService.updateScreen(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteScreen(@PathVariable Long id){
        screenService.deleteScreen(id);
        return ResponseEntity.noContent().build();
    }

}
