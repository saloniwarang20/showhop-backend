package com.example.eventbook_backend.controller;

import com.example.eventbook_backend.dto.movieShow.MovieShowRequest;
import com.example.eventbook_backend.dto.movieShow.MovieShowResponse;
import com.example.eventbook_backend.dto.showSeat.ShowSeatResponse;
import com.example.eventbook_backend.service.MovieShowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/movie-shows")
@RequiredArgsConstructor
public class MovieShowController {

    private final MovieShowService movieShowService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieShowResponse> createShow(@Valid @RequestBody MovieShowRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(movieShowService.createShow(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieShowResponse> getShowById(@PathVariable Long id){
        return ResponseEntity.ok(movieShowService.getShowById(id));
    }

    @GetMapping
    public ResponseEntity<List<MovieShowResponse>> getAllShows(){
        return ResponseEntity.ok(movieShowService.getAllShows());
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<MovieShowResponse>> getShowsByMovie(@PathVariable Long movieId){
        return ResponseEntity.ok(movieShowService.getShowsByMovie(movieId));
    }

    @GetMapping("/screen/{screenId}")
    public ResponseEntity<List<MovieShowResponse>> getShowsByScreen(@PathVariable Long screenId){
        return ResponseEntity.ok(movieShowService.getShowsByScreen(screenId));
    }

    @GetMapping("/movie/{movieId}/date/{date}")
    public ResponseEntity<List<MovieShowResponse>> getShowsByMovieAndDate(@PathVariable Long movieId, @PathVariable LocalDate date){
        return ResponseEntity.ok(movieShowService.getShowsByMovieAndDate(movieId, date));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieShowResponse> updateShow(@PathVariable Long id, @Valid @RequestBody MovieShowRequest request){
        return ResponseEntity.ok(movieShowService.updateShow(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteShow(@PathVariable Long id){
        movieShowService.deleteShow(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/seats")
    public ResponseEntity<List<ShowSeatResponse>> getSeatsForShow(@PathVariable Long id){
        return ResponseEntity.ok(movieShowService.getSeatsForShow(id));
    }
}
