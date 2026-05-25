package com.hoaug.movieapi.common.response;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.hoaug.movieapi.common.dto.ApiErrorResponse;

public class ResponseUtil {

  public static <T> ResponseEntity<T> created (T data) {
    String location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
    return created(data, location);
  }

  public static <T> ResponseEntity<T> created (T data, String location) {
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(ServletUriComponentsBuilder.fromUriString(location).build().toUri());
    return new ResponseEntity<>(data, headers, HttpStatus.CREATED);
  }

  public static <T> ResponseEntity<T> ok (T data) {
    return ResponseEntity.ok(data);
  }

  public static ResponseEntity<Void> noContent () {
    return ResponseEntity.noContent().build();
  }

  public static ResponseEntity<ApiErrorResponse> badRequest (String message) {
    ApiErrorResponse error = ApiErrorResponse.builder().code("BAD_REQUEST").message(message)
        .timestamp(System.currentTimeMillis()).build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  public static ResponseEntity<ApiErrorResponse> badRequest (String message, Object details) {
    ApiErrorResponse error = ApiErrorResponse.builder().code("BAD_REQUEST").message(message)
        .details(details).timestamp(System.currentTimeMillis()).build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  public static ResponseEntity<ApiErrorResponse> unauthorized (String message) {
    ApiErrorResponse error = ApiErrorResponse.builder().code("UNAUTHORIZED").message(message)
        .timestamp(System.currentTimeMillis()).build();
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
  }

  public static ResponseEntity<ApiErrorResponse> forbidden (String message) {
    ApiErrorResponse error = ApiErrorResponse.builder().code("FORBIDDEN").message(message)
        .timestamp(System.currentTimeMillis()).build();
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
  }

  public static ResponseEntity<ApiErrorResponse> notFound (String message) {
    ApiErrorResponse error = ApiErrorResponse.builder().code("NOT_FOUND").message(message)
        .timestamp(System.currentTimeMillis()).build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  public static ResponseEntity<ApiErrorResponse> conflict (String message) {
    ApiErrorResponse error = ApiErrorResponse.builder().code("CONFLICT").message(message)
        .timestamp(System.currentTimeMillis()).build();
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
  }

  
  
  public static ResponseEntity<ApiErrorResponse> unprocessable (String message) {
    ApiErrorResponse error = ApiErrorResponse.builder().code("UNPROCESSABLE_ENTITY")
        .message(message).timestamp(System.currentTimeMillis()).build();
    return ResponseEntity.status(HttpStatusCode.valueOf(422)).body(error);
  }

  public static ResponseEntity<ApiErrorResponse> internalError (String message) {
    ApiErrorResponse error = ApiErrorResponse.builder().code("INTERNAL_ERROR").message(message)
        .timestamp(System.currentTimeMillis()).build();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}
