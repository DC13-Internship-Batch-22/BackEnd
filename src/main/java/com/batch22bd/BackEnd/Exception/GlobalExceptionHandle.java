package com.batch22bd.BackEnd.Exception;

import com.batch22bd.BackEnd.DTO.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException e,
            HttpServletRequest request
    ) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                request.getRequestURI(),
                Instant.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(
            ConflictException e,
            HttpServletRequest request
    ) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                request.getRequestURI(),
                Instant.now()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
