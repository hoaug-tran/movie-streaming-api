package com.hoaug.movieapi.common.handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(value = AppException.class)
  public ResponseEntity<Object> handlingAppException (AppException exception) {
    ErrorCode errorCode = exception.getErrorCode();
    logger.warn("AppException caught: {} - {}", errorCode, exception.getMessage());

    return ResponseEntity.status(errorCode.getStatusCode()).body(errorCode.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handlingValidationException (
      MethodArgumentNotValidException exception) {
    Map<String, String> errors = new HashMap<>();
    exception.getBindingResult().getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
    logger.warn("Validation error: {}", errors);
    return ResponseEntity.status(400).body(errors);
  }

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<Object> handlingUnexpectedException (Exception exception) {
    logger.error("Unexpected exception: ", exception);
    return ResponseEntity.status(500).body("Internal Server Error");
  }
}