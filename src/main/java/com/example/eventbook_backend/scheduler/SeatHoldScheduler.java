package com.example.eventbook_backend.scheduler;

import com.example.eventbook_backend.service.SeatHoldService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeatHoldScheduler {

    private final SeatHoldService seatHoldService;

    @Scheduled(fixedDelay = 30000)
    public void releaseScheduler(){
        seatHoldService.releaseExpiredSeats();
    }
}
