package com.example.eventbook_backend.service;

import com.example.eventbook_backend.dto.payment.PaymentResponse;

public interface PaymentService {

    PaymentResponse createPayment(Long bookingId);

    PaymentResponse completePayment(Long bookingId);


}
