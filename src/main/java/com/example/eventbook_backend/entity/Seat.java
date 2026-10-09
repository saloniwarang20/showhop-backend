package com.example.eventbook_backend.entity;

import com.example.eventbook_backend.enums.SeatType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "seats",
        uniqueConstraints = {@UniqueConstraint(name = "uk_seat_screen_number", columnNames = {"screen_id", "seatNumber"})})
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String seatNumber;

    @Column(name = "`row_number`",nullable = false)
    private String rowNumber;

    @Enumerated(EnumType.STRING)
    @Column(name="seat_type",nullable = false)
    private SeatType seatType;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
