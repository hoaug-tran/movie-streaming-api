package com.hoaug.movieapi.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SafeStringValidator implements ConstraintValidator<ValidSafeString, String> {

  private static final String HTML_PATTERN = "[<>\"'%;()&+]";
  private static final String SCRIPT_PATTERN = "(script|iframe|onclick|onerror|onload|javascript:)";
  private int minLength;
  private int maxLength;

  @Override
  public void initialize (ValidSafeString constraint) {
    this.minLength = constraint.minLength();
    this.maxLength = constraint.maxLength();
  }

  @Override
  public boolean isValid (String value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    if (value.length() < minLength || value.length() > maxLength) {
      return false;
    }

    if (value.matches(".*" + HTML_PATTERN + ".*")) {
      return false;
    }

    if (value.toLowerCase().matches(".*" + SCRIPT_PATTERN + ".*")) {
      return false;
    }

    return true;
  }
}
