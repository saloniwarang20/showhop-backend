package com.example.eventbook_backend.repository;

import com.example.eventbook_backend.entity.MovieCast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieCastRepository extends JpaRepository<MovieCast, Long> {

    List<MovieCast> findByMovieId(Long movieId);

}
