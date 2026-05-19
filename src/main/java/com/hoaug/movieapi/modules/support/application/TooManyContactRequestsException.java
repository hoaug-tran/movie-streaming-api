package com.hoaug.movieapi.modules.support.application;

public class TooManyContactRequestsException extends RuntimeException {
  public TooManyContactRequestsException(String message) {
    super(message);
  }
}
