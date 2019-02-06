package ua.in.javac.web;

import ua.in.javac.exceptions.CredentialNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice(basePackages = "ua.in.javac.web")
public class WebAppExceptionHandler {
!!!
    @ExceptionHandler(CredentialNotFoundException.class)
    @ResponseBody
    public String userNotFound(CredentialNotFoundException e) {
        return e.getMessage();
    }
}
