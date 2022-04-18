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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@ControllerAdvice
public class ControllerErrorHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder().status(BAD_REQUEST).message("Validation errors").build();
        ex.getBindingResult().getFieldErrors().stream().map(ValidationError::fromFieldError).forEach(errorResponse::addError);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse> handleArgumentTypeMismatchError(MethodArgumentTypeMismatchException ex) {
        String message = String.format(ErrorMessage.TYPE_ERROR, ex.getName(), ex.getRequiredType().getName());
        ErrorResponse errorResponse = new ErrorResponse.ErrorResponseBuilder()
                .status(BAD_REQUEST)
                .message(message).build();
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
