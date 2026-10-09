package com.example.eventbook_backend.mapper;

import com.example.eventbook_backend.dto.review.ReviewResponse;
import com.example.eventbook_backend.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewResponse toResponse(Review review){
        String username = review.getUser().getFirstName() +" "+ review.getUser().getLastName();
        return ReviewResponse.builder()
                .id(review.getId())
                .username(username)
                .movieId(review.getMovie().getId())
                .movieTitle(review.getMovie().getTitle())
                .rating(review.getRating())
                .comment(review.getComment())
                .date(review.getCreatedAt())
                .build();
    }
}
