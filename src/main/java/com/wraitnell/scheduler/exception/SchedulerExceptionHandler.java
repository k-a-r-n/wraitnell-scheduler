package com.wraitnell.scheduler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SchedulerExceptionHandler {

    @ExceptionHandler(SchedulerException.class)
    public ResponseEntity<Object> handleSchedulerException (SchedulerException e) {
        return new ResponseEntity<>(e.getMessage(),e.getStatus());
    }

}
