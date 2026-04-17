package ru.shaporenko.intern.task_time_tracker.exception.handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.shaporenko.intern.task_time_tracker.exception.BusinessRuleException;
import ru.shaporenko.intern.task_time_tracker.exception.ErrorResponse;
import ru.shaporenko.intern.task_time_tracker.exception.InvalidParameterException;
import ru.shaporenko.intern.task_time_tracker.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException e) {
        return buildResponse(HttpStatus.NOT_FOUND, e, LocalDateTime.now());
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<ErrorResponse> handleInvalidParameter(
            InvalidParameterException e) {
        return buildResponse(HttpStatus.BAD_REQUEST, e, LocalDateTime.now());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(
            ConstraintViolationException e){
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            errors.put(fieldName, violation.getMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRule(BusinessRuleException e){
        return buildResponse(HttpStatus.CONFLICT, e, LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception e) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, e, LocalDateTime.now());
    }

    public ResponseEntity<ErrorResponse> buildResponse(HttpStatus status,
                                                       Throwable throwable, LocalDateTime timestamp) {
        ErrorResponse errorResponse = new ErrorResponse(
                status.getReasonPhrase(), throwable.getMessage(), timestamp);
        return ResponseEntity.status(status).body(errorResponse);
    }

}
