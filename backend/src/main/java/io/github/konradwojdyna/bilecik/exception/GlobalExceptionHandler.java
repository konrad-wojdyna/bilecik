package io.github.konradwojdyna.bilecik.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
               "Request validation failed"
        );

        problemDetail.setTitle("Invalid request");
        problemDetail.setType(URI.create("https://bilecik.dev/problems/validation-error"));
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("errors", errors);

        return problemDetail;
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ProblemDetail handleEventNotFoundException(
            EventNotFoundException ex,
            HttpServletRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );

        problemDetail.setTitle("Event not found");
        problemDetail.setType(URI.create("https://bilecik.dev/problems/event-not-found"));
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return problemDetail;
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ProblemDetail handlePropertyReferenceException(
            PropertyReferenceException ex,
            HttpServletRequest request
    ){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_CONTENT,
                "Unknown sort property"
        );

        problemDetail.setTitle("Sorting by " + ex.getPropertyName() + " is not supported");
        problemDetail.setType(URI.create("https://bilecik.dev/problems/unknown-property"));
        problemDetail.setProperty("property", "Property " + ex.getPropertyName() + " not found");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return problemDetail;
    }
}
