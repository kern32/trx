package ua.in.javac.web;

import ua.in.javac.exceptions.UserNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice(basePackages = "ua.in.javac.web")
public class WebAppExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    public String userNotFound(UserNotFoundException e) {
        return e.getMessage();
    }
}
