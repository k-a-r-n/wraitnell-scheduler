package com.wraitnell.scheduler.exception;

import org.springframework.http.HttpStatus;

public class SchedulerException extends RuntimeException{

    public SchedulerException(String message) {
        super(message);
    }

    public SchedulerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SchedulerException(Throwable cause) {
        super(cause);
    }

    public SchedulerException() {
    }

    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
