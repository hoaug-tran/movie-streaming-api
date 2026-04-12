package com.hoaug.movieapi.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {

  private String code;

  private String message;

  private Object details;

  private long timestamp;

  private String errorId;

  public static ApiErrorResponse of (String code, String message) {
    return ApiErrorResponse.builder().code(code).message(message)
        .timestamp(System.currentTimeMillis()).build();
  }

  public static ApiErrorResponse of (String code, String message, Object details) {
    return ApiErrorResponse.builder().code(code).message(message).details(details)
        .timestamp(System.currentTimeMillis()).build();
  }
}
