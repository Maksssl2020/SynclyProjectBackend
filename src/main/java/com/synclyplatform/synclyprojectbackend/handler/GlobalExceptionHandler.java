package com.synclyplatform.synclyprojectbackend.handler;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(
                        createExceptionResponse(HttpStatus.FORBIDDEN, "Forbidden", "Nieprawidłowe dane.", request.getRequestURI())
                );
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ExceptionResponse> handleExpiredJwtException(ExpiredJwtException error, HttpServletRequest request) {
        return ResponseEntity.status(UNAUTHORIZED)
                .body(
                        createExceptionResponse(UNAUTHORIZED, "Unauthorized", error.getMessage(), request.getRequestURI())
                );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception error, HttpServletRequest request) {
        logger.error(error.getMessage(), error);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        createExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", error.getMessage(), request.getRequestURI())
                );
    }

    private ExceptionResponse createExceptionResponse(HttpStatus status, String error, String message, String path) {
        return ExceptionResponse.builder()
                .timestamp(Instant.now().toEpochMilli())
                .status(status.value())
                .message(message)
                .error(error)
                .path(path)
                .build();
    }
}
