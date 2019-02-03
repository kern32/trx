package ua.in.javac.rest.controller;

import ua.in.javac.exceptions.CredentialNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "ua.in.javac.rest")
public class RestAppControllerAdvice {

    @ExceptionHandler(CredentialNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error userNotFound(CredentialNotFoundException e) {
        return new Error(e.getMessage());
    }
}
