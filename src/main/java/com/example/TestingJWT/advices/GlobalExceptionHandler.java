package com.example.TestingJWT.advices;


import com.example.TestingJWT.exceptions.ResourceNotFoundException;
import com.example.TestingJWT.exceptions.RuntimeConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex)
    {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>( apiError.getStatus());
    }

    @ExceptionHandler(RuntimeConflictException.class)
    public ResponseEntity<ApiError> handleRuntimeConflictException(RuntimeConflictException ex)
    {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>( apiError.getStatus());
    }


}
