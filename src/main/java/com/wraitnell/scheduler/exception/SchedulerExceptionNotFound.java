package com.wraitnell.scheduler.exception;

import org.springframework.http.HttpStatus;

public class SchedulerExceptionNotFound extends SchedulerException{
    // Exception thrown when resource not found
    public SchedulerExceptionNotFound(String message) {
        super(message);
    }
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
