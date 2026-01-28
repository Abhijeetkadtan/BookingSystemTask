package com.example.booking.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerUnitTest {
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    GlobalExceptionHandlerUnitTest() {
    }

    @Test
    void handleNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Booking not found");
        ResponseEntity<ErrorResponse> response = this.handler.handleNotFound(ex);
        Assertions.assertEquals(404, ((ErrorResponse)response.getBody()).getStatus());
        Assertions.assertEquals("Booking not found", ((ErrorResponse)response.getBody()).getMessage());
    }

    @Test
    void handleUnauthorized() {
        UnauthorizedActionException ex = new UnauthorizedActionException("Access denied");
        ResponseEntity<ErrorResponse> response = this.handler.handleUnauthorized(ex);
        Assertions.assertEquals(403, ((ErrorResponse)response.getBody()).getStatus());
        Assertions.assertEquals("Access denied", ((ErrorResponse)response.getBody()).getMessage());
    }

    @Test
    void handleConflict() {
        SlotAlreadyBookedException ex = new SlotAlreadyBookedException("Slot already booked");
        ResponseEntity<ErrorResponse> response = this.handler.handleConflict(ex);
        Assertions.assertEquals(409, ((ErrorResponse)response.getBody()).getStatus());
        Assertions.assertEquals("Slot already booked", ((ErrorResponse)response.getBody()).getMessage());
    }

    @Test
    void handleGeneralException() {
        Exception ex = new Exception("Unexpected error");
        ResponseEntity<ErrorResponse> response = this.handler.handleGeneral(ex);
        Assertions.assertEquals(500, ((ErrorResponse)response.getBody()).getStatus());
        Assertions.assertEquals("Something went wrong", ((ErrorResponse)response.getBody()).getMessage());
    }
}
