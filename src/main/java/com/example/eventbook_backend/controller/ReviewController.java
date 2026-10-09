package com.example.eventbook_backend.controller;

import com.example.eventbook_backend.dto.review.ReviewRequest;
import com.example.eventbook_backend.dto.review.ReviewResponse;
import com.example.eventbook_backend.dto.review.TopRatedMovieResponse;
import com.example.eventbook_backend.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody ReviewRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable Long id){
        return ResponseEntity.ok(reviewService.getReview(id));
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ReviewResponse>> getMovieReviews(@PathVariable Long movieId){
        return ResponseEntity.ok(reviewService.getMovieReviews(movieId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewRequest request){
        return ResponseEntity.ok(reviewService.updateReview(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id){
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/top-rated")
    public ResponseEntity<Page<TopRatedMovieResponse>> getTopRatedMovies(@PageableDefault(size=10)Pageable pageable){
        return ResponseEntity.ok(reviewService.getTopRatedMovies(pageable));
    }
}
