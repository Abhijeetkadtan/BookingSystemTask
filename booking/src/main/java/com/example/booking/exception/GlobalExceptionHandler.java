package com.example.booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public GlobalExceptionHandler() {
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity(new ErrorResponse(404, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UnauthorizedActionException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedActionException ex) {
        return new ResponseEntity(new ErrorResponse(403, ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({SlotAlreadyBookedException.class})
    public ResponseEntity<ErrorResponse> handleConflict(SlotAlreadyBookedException ex) {
        return new ResponseEntity(new ErrorResponse(409, ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        return new ResponseEntity(new ErrorResponse(500, "Something went wrong"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
