package com.musala.calendar.exeptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(NotFoundInDBException.class)
    public final ResponseEntity<ErrorDetails> handleNotFoundInDBException(NotFoundInDBException ex) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                "Entity Not Found",
                ex.getLocalizedMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetails> handleException(Exception ex) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                "Error",
                ex.getLocalizedMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
