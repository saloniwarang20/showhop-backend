package com.example.eventbook_backend.service;

import com.example.eventbook_backend.dto.review.ReviewRequest;
import com.example.eventbook_backend.dto.review.ReviewResponse;
import com.example.eventbook_backend.dto.review.TopRatedMovieResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {

    ReviewResponse createReview(ReviewRequest request);

    ReviewResponse getReview(Long id);

    List<ReviewResponse> getMovieReviews(Long movieId);

    ReviewResponse updateReview(Long reviewId, ReviewRequest request);

    void deleteReview(Long reviewId);

    Page<TopRatedMovieResponse> getTopRatedMovies(Pageable pageable);
}
