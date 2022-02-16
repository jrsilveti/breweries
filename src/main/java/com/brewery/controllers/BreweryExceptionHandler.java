package com.brewery.controllers;

import com.brewery.models.BreweryError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class BreweryExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ResponseStatusException.class})
    protected ResponseEntity<BreweryError> handleResponseError(ResponseStatusException exception) {
        BreweryError error = new BreweryError();
        if(exception.getStatus().is5xxServerError()) {
            error.setStatus(500);
            error.setMessage("The server cannot be reached");
        } else if (exception.getStatus().equals(HttpStatus.NO_CONTENT)){
            error.setStatus(204);
        }
        return new ResponseEntity<>(error, HttpStatus.valueOf(error.getStatus()));
    }
}
