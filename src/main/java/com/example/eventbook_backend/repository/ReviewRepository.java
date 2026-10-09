package com.example.eventbook_backend.repository;

import com.example.eventbook_backend.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByUserIdAndMovieId(Long userId, Long movieId);

    List<Review> findByMovieId(Long movieId);

    @Query("""
        SELECT r.movie.id, AVG(r.rating), COUNT(r.id)
        FROM Review r
        GROUP BY r.movie.id
        ORDER BY AVG(r.rating) DESC
    """)
    Page<Object[]> findTopRatedMovies(Pageable pageable);
}