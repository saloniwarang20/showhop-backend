package com.example.eventbook_backend.service.impl;

import com.example.eventbook_backend.dto.review.ReviewRequest;
import com.example.eventbook_backend.dto.review.ReviewResponse;
import com.example.eventbook_backend.dto.review.TopRatedMovieResponse;
import com.example.eventbook_backend.entity.Movie;
import com.example.eventbook_backend.entity.Review;
import com.example.eventbook_backend.entity.User;
import com.example.eventbook_backend.enums.BookingStatus;
import com.example.eventbook_backend.exception.IllegalStateException;
import com.example.eventbook_backend.exception.ResourceAlreadyExistsException;
import com.example.eventbook_backend.exception.ResourceNotFoundException;
import com.example.eventbook_backend.mapper.ReviewMapper;
import com.example.eventbook_backend.repository.BookingRepository;
import com.example.eventbook_backend.repository.MovieRepository;
import com.example.eventbook_backend.repository.ReviewRepository;
import com.example.eventbook_backend.repository.UserRepository;
import com.example.eventbook_backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    }

    @Override
    @Transactional
    public ReviewResponse createReview(ReviewRequest request) {

        User user = getCurrentUser();

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        if(request.getRating() == null || request.getRating() < 1|| request.getRating() > 5){
            throw new IllegalStateException("Rating must be between 1 and 5");
        }

        if(reviewRepository.existsByUserIdAndMovieId(user.getId(), movie.getId())){
            throw new ResourceAlreadyExistsException("You have already reviewed this movie");
        }

        if (!bookingRepository.existsByUserIdAndMovieShowMovieIdAndStatus(user.getId(), movie.getId(), BookingStatus.CONFIRMED)) {
            throw new IllegalStateException(
                    "You can only review a movie after booking it"
            );
        }

        Review review = Review.builder()
                .user(user)
                .movie(movie)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        Review savedReview = reviewRepository.save(review);

        return reviewMapper.toResponse(savedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponse getReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        return reviewMapper.toResponse(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getMovieReviews(Long movieId) {
        movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        return reviewRepository.findByMovieId(movieId)
                .stream()
                .map(reviewMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ReviewResponse updateReview(Long reviewId, ReviewRequest request) {

        User user = getCurrentUser();

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if(!review.getUser().getId().equals(user.getId())){
            throw new IllegalStateException("You cannot update this review");
        }

        if(request.getRating() == null || request.getRating() < 1|| request.getRating() > 5){
            throw new IllegalStateException("Rating must be between 1 and 5");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review updatedReview = reviewRepository.save(review);

        return reviewMapper.toResponse(updatedReview);
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId) {

        User user = getCurrentUser();

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if(!review.getUser().getId().equals(user.getId())){
            throw new IllegalStateException("You cannot delete this review");
        }

        reviewRepository.delete(review);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<TopRatedMovieResponse> getTopRatedMovies(Pageable pageable) {
        return reviewRepository.findTopRatedMovies(pageable)
                .map(result -> new TopRatedMovieResponse(
                        ((Number) result[0]).longValue(),
                        ((Number) result[1]).doubleValue(),
                        ((Number) result[2]).longValue()
                ));
    }
}
