package com.example.booking.controller;

import com.example.booking.service.BookingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/admin/bookings"})
public class AdminBookingController {
    private final BookingService bookingService;

    public AdminBookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping({"/{id}/cancel"})
    @PreAuthorize("hasRole('ADMIN')")
    public void cancelAny(@PathVariable Long id) {
        this.bookingService.adminCancelBooking(id);
    }
}
