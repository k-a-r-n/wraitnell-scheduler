package com.wraitnell.scheduler.exception;

import org.springframework.http.HttpStatus;

public class SchedulerExceptionBadRequest extends SchedulerException{
    // Exception thrown when resource not found
    public SchedulerExceptionBadRequest(String message) {
        super("Bad request. Reason: " + message);
    }
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
