package com.musala.calendar.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundInDBException extends RuntimeException {
    public NotFoundInDBException(String message) {
        super(message);
    }
}
