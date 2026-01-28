package com.example.booking.controller;

import com.example.booking.entity.Booking;
import com.example.booking.entity.BookingStatus;
import com.example.booking.service.BookingService;
import java.nio.file.AccessDeniedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

class BookingControllerUnitTest {
    @Mock
    private BookingService bookingService;
    @InjectMocks
    private BookingController bookingController;

    BookingControllerUnitTest() {
    }

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void bookSlot_success() throws AccessDeniedException {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStatus(BookingStatus.ACTIVE);
        Mockito.when(this.bookingService.bookSlot((Long)ArgumentMatchers.any(), (Long)ArgumentMatchers.any(), (String)ArgumentMatchers.any())).thenReturn(booking);
        Authentication auth = new UsernamePasswordAuthenticationToken("test-user", (Object)null);
        Booking response = this.bookingController.book(1L, 1L, auth);
        Assertions.assertEquals(BookingStatus.ACTIVE, response.getStatus());
    }
}
