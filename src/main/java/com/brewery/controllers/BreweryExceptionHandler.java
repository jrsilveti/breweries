package com.brewery.controllers;

import com.brewery.models.BreweryError;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class BreweryExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ResponseStatusException.class})
    protected BreweryError handleResponseError(HttpStatus status) {
        BreweryError error = new BreweryError();
        if(status.is5xxServerError()) {
            error.setStatus(500);
            error.setMessage("The server cannot be reached");
        }
        return error;
    }
}
