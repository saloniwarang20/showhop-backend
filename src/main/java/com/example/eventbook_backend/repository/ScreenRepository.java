package com.example.eventbook_backend.repository;

import com.example.eventbook_backend.entity.Screen;
import com.example.eventbook_backend.entity.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScreenRepository extends JpaRepository<Screen, Long> {

    List<Screen> findByTheatreId(Long theatreId);

    boolean existsByTheatreIdAndName(Long theatreId, String name);

    boolean existsByTheatreIdAndNameAndIdNot(
            Long theatreId,
            String name,
            Long id
    );
}
