package com.exercise.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@ControllerAdvice
public class ControllerErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder().status(BAD_REQUEST).message("Validation errors").build();
        ex.getBindingResult().getFieldErrors().stream().map(ValidationError::fromFieldError).forEach(errorResponse::addError);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> invalidJasonFormat(HttpMessageNotReadableException exception) {
        log.error("Invalid input ", exception);
        ErrorResponse errorResponse = new ErrorResponse.ErrorResponseBuilder()
                .status(BAD_REQUEST)
                .message(ErrorMessage.INVALID_JSON_FORMAT).build();
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> unknownError(Exception exception) {
        log.error("Unknown error ", exception);
        ErrorResponse errorResponse = new ErrorResponse.ErrorResponseBuilder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(ErrorMessage.UNKNOWN_ERROR).build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
