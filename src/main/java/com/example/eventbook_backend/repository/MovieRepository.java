package com.example.eventbook_backend.repository;

import com.example.eventbook_backend.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {

    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, Long id);
}
