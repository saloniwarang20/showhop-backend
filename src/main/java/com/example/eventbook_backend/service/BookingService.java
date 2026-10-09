package com.example.eventbook_backend.service;

import com.example.eventbook_backend.dto.booking.BookingRequest;
import com.example.eventbook_backend.dto.booking.BookingResponse;

import java.util.List;

public interface BookingService {

    BookingResponse createBooking(BookingRequest request);

    BookingResponse getBookingById(Long id);

    BookingResponse  getBookingByBookingNumber(String bookingNumber);

    List<BookingResponse> getMyBookings();

    void cancelBooking(Long id);

    void expireBookings();
}
