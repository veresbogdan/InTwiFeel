package intwifeel.controller;

import intwifeel.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse errorResponse(Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }
}

