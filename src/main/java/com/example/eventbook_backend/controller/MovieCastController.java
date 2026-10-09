package com.example.eventbook_backend.controller;

import com.example.eventbook_backend.dto.moviecast.MovieCastRequest;
import com.example.eventbook_backend.dto.moviecast.MovieCastResponse;
import com.example.eventbook_backend.service.MovieCastService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieCastController {

    private final MovieCastService movieCastService;

    @PostMapping("/{movieId}/cast")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieCastResponse> addCast(@PathVariable Long movieId, @Valid @RequestBody MovieCastRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(movieCastService.addCast(movieId, request));
    }

    @GetMapping("/{movieId}/cast")
    public ResponseEntity<List<MovieCastResponse>> getMovieCast(@PathVariable Long movieId) {
        return ResponseEntity.ok(movieCastService.getMovieCast(movieId));
    }

    @PutMapping("/cast/{castId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieCastResponse> updateCast(@PathVariable Long castId, @Valid @RequestBody MovieCastRequest request) {
        return ResponseEntity.ok(movieCastService.updateCast(castId, request));
    }

    @DeleteMapping("/cast/{castId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCast(@PathVariable Long castId) {
        movieCastService.deleteCast(castId);
        return ResponseEntity.noContent().build();
    }
}
