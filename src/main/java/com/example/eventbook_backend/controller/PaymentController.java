package com.example.eventbook_backend.controller;

import com.example.eventbook_backend.dto.payment.PaymentResponse;
import com.example.eventbook_backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{bookingId}")
    public ResponseEntity<PaymentResponse> createPayment(@PathVariable Long bookingId){
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment(bookingId));
    }

    @PostMapping("/{bookingId}/complete")
    public ResponseEntity<PaymentResponse> completePayment(@PathVariable Long bookingId){
        return ResponseEntity.ok(paymentService.completePayment(bookingId));
    }
}
