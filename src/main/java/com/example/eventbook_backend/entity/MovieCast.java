package com.example.eventbook_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "movie_cast")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieCast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String actorName;

    private String characterName;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;
}
