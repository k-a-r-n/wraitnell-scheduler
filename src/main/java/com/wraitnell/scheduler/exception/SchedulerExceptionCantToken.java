package com.wraitnell.scheduler.exception;

import org.springframework.http.HttpStatus;

public class SchedulerExceptionCantToken extends SchedulerException{
    // Exception thrown when resource not found
    public SchedulerExceptionCantToken(String message) {
        super("Can not token player for reason: " + message);
    }
    public HttpStatus getStatus() {
        return HttpStatus.NOT_ACCEPTABLE;
    }
}
