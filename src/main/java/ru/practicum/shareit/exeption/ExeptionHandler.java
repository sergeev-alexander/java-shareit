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

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExeptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> methodArgumentNotValidHendle(MethodArgumentNotValidException e) {
        Map<String, String> response = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            response.put(error.getField(), error.getDefaultMessage());
            log.error("Validation error in field {}", response.entrySet());
        }
        return response;
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> validatoinHandle(ValidationException e) {
        log.error("{} : {}", e.getClass().getSimpleName(), e.getMessage());
        return new ResponseEntity<>(e.getClass().getSimpleName() + " : " + e.getMessage(),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> notFoundHandle(NotFoundException e) {
        log.error("{} : {}", e.getClass().getSimpleName(), e.getMessage());
        return new ResponseEntity<>(e.getClass().getSimpleName() + " : " + e.getMessage(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            MissingRequestHeaderException.class,
            ConstraintViolationException.class,})
    public ResponseEntity<String> exceptionHandle(Exception e) {
        log.error("{} : {}", e.getClass().getSimpleName(), e.getMessage());
        return new ResponseEntity<>(e.getClass().getSimpleName() + " : " + e.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> throwableHandle(Throwable e) {
        log.error("{} : {}", e.getClass().getSimpleName(), e.getMessage());
        return new ResponseEntity<>(e.getClass().getSimpleName() + " : " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
