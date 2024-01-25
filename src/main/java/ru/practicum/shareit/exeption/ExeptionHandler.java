package ru.practicum.shareit.exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExeptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validationHendle(MethodArgumentNotValidException e) {
        Map<String, String> response = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            response.put(error.getField(), error.getDefaultMessage());
        }
        log.error("Validation error: {}", response.entrySet());
        return response;
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> constraintViolationHandle(ValidationException e) {
        log.error("{} : {}", e.getClass().getSimpleName(), e.getMessage());
        return new ResponseEntity<>(e.getClass().getSimpleName() + e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> notFoundHandle(NotFoundException e) {
        log.error("{} : {}", e.getClass().getSimpleName(), e.getMessage());
        return new ResponseEntity<>(e.getClass().getSimpleName() + e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<String> notFoundHandle(MissingRequestHeaderException e) {
        log.error("{} : {}", e.getClass().getSimpleName(), e.getMessage());
        return new ResponseEntity<>(e.getClass().getSimpleName() + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
