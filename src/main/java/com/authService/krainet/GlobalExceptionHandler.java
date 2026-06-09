package com.authService.krainet;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(
            Exception e
    ) {
        log.error("Handle exception", e);
        return ResponseEntity
                .status(500)
                .body(e.getMessage());
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(
            EntityNotFoundException e
    ) {
        log.error("Handle EntityNotFoundException", e);
        return ResponseEntity
                .status(404)
                .body(e.getMessage());
    }
    @ExceptionHandler(exception = {
            IllegalArgumentException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<String> handleBadRequest(
            IllegalArgumentException e
    ) {
        log.error("Handle BadRequest", e);
        return ResponseEntity
                .status(400)
                .body(e.getMessage());
    }

}
