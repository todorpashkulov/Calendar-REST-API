package com.musala.calendar.exeptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

@SuppressWarnings("unused")
public class ErrorDetails {

    private LocalDateTime currentTime;
    private String status;
    private String message;
    private String details;

    public ErrorDetails(LocalDateTime currentTime, HttpStatus status, String message, String details) {
        this.currentTime = currentTime;
        this.status = status.toString();
        this.message = message;
        this.details = details;
    }

    public LocalDateTime getCurrentTime() {
        return currentTime;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}