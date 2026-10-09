package com.example.eventbook_backend.repository;

import com.example.eventbook_backend.entity.MovieShow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MovieShowRepository extends JpaRepository<MovieShow, Long> {

    List<MovieShow> findByMovieId(Long movieId);

    List<MovieShow> findByScreenId(Long screenId);

    List<MovieShow> findByMovieIdAndShowDate(Long movieId, LocalDate showDate);

    @Query("""
    SELECT COUNT(ms) > 0
    FROM MovieShow ms
    WHERE ms.screen.id = :screenId
      AND ms.showDate = :showDate
      AND ms.startTime < :endTime
      AND ms.endTime > :startTime
""")
    boolean existsOverlappingShow(
            @Param("screenId") Long screenId,
            @Param("showDate") LocalDate showDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @Query("""
    SELECT COUNT(ms) > 0
    FROM MovieShow ms
    WHERE ms.screen.id = :screenId
      AND ms.showDate = :showDate
      AND ms.id != :showId
      AND ms.startTime < :endTime
      AND ms.endTime > :startTime
""")
    boolean existsOverlappingShowForUpdate(
            @Param("showId") Long showId,
            @Param("screenId") Long screenId,
            @Param("showDate") LocalDate showDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}
