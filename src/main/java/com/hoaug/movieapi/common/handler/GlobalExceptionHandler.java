package com.hoaug.movieapi.common.handler;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.hoaug.movieapi.common.dto.ApiErrorResponse;
import com.hoaug.movieapi.common.dto.FieldError;
import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(value = AppException.class)
  public ResponseEntity<ApiErrorResponse> handlingAppException (AppException exception) {
    ErrorCode errorCode = exception.getErrorCode();
    logger.warn("AppException caught: {} - {}", errorCode.name(), exception.getMessage());

    ApiErrorResponse errorResponse = ApiErrorResponse.builder().code(errorCode.name())
        .message(exception.getMessage()).timestamp(System.currentTimeMillis())
        .errorId(UUID.randomUUID().toString()).build();

    return ResponseEntity.status(errorCode.getStatusCode()).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handlingValidationException (
      MethodArgumentNotValidException exception) {

    List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors().stream()
        .map(error -> FieldError.builder().field(error.getField())
            .message(error.getDefaultMessage()).rejectedValue(error.getRejectedValue()).build())
        .collect(Collectors.toList());

    logger.warn("Validation error: {} fields failed validation", fieldErrors.size());

    ApiErrorResponse errorResponse = ApiErrorResponse.builder().code("VALIDATION_ERROR")
        .message("Validation failed").details(fieldErrors).timestamp(System.currentTimeMillis())
        .errorId(UUID.randomUUID().toString()).build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiErrorResponse> handlingAccessDeniedException (
      AccessDeniedException exception) {
    logger.warn("Access denied: {}", exception.getMessage());

    ApiErrorResponse errorResponse = ApiErrorResponse.builder().code("FORBIDDEN")
        .message("You do not have permission to access this resource")
        .timestamp(System.currentTimeMillis()).errorId(UUID.randomUUID().toString()).build();

    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ApiErrorResponse> handlingNoHandlerFoundException (
      NoHandlerFoundException exception) {
    logger.warn("Endpoint not found: {} {}", exception.getRequestURL(), exception.getMessage());

    ApiErrorResponse errorResponse = ApiErrorResponse.builder().code("NOT_FOUND")
        .message("Endpoint not found").timestamp(System.currentTimeMillis())
        .errorId(UUID.randomUUID().toString()).build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<ApiErrorResponse> handlingUnexpectedException (Exception exception) {
    String errorId = UUID.randomUUID().toString();
    logger.error("Unexpected exception [errorId={}]: ", errorId, exception);

    ApiErrorResponse errorResponse = ApiErrorResponse.builder().code("INTERNAL_ERROR")
        .message("An unexpected error occurred. Please contact support with error ID: " + errorId)
        .timestamp(System.currentTimeMillis()).errorId(errorId).build();

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }
}