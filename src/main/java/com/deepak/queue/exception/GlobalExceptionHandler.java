package com.deepak.queue.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        LOGGER.error("An error occurred: {}", ex.getMessage());
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred. Please try again later.");
    }

    @ExceptionHandler({ValidationException.class, WebExchangeBindException.class,
            MethodArgumentNotValidException.class, ResponseStatusException.class,
            ConstraintViolationException.class})
    public ResponseEntity<Object> handleValidationExceptions(Exception ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().format(formatter));
        HttpStatus status = HttpStatus.BAD_REQUEST; // Default status

        List<String> errors = new ArrayList<>();

        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException validationException = (MethodArgumentNotValidException) ex;
            errors = validationException.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
        } else if (ex instanceof WebExchangeBindException) {
            WebExchangeBindException bindException = (WebExchangeBindException) ex;
            errors = bindException.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
        } else if (ex instanceof ResponseStatusException) {
            if (((ResponseStatusException) ex).getStatusCode().value() == 404) {
                status = HttpStatus.NOT_FOUND;
                errors.add("Not Found");
            } else {
                errors.add(((ResponseStatusException) ex).getReason());
            }

        } else {
            // Handle other validation-related exceptions here
            // For example, if ValidationException is another custom exception
            errors.add(ex.getMessage()); // Add the exception message to errors
        }

        body.put("status", status.value());
        if (!errors.isEmpty()) {
            body.put("errors", errors);
        }

        return new ResponseEntity<>(body, status);
    }

}