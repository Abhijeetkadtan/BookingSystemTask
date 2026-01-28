package com.example.booking.controller;

import com.example.booking.entity.Booking;
import com.example.booking.service.BookingService;
import java.nio.file.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/bookings"})
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public Booking book(@RequestParam Long slotId, @RequestParam Long userId, Authentication authentication) throws AccessDeniedException {
        return this.bookingService.bookSlot(slotId, userId, authentication.getName());
    }

    @PostMapping({"/{id}/cancel"})
    @PreAuthorize("hasRole('USER')")
    public void cancel(@PathVariable Long id, @RequestParam Long userId, Authentication authentication) throws AccessDeniedException {
        this.bookingService.cancelBooking(id, userId, authentication.getName());
    }
}
