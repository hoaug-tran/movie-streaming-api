package com.hoaug.movieapi.modules.searchhistory.application.dto.request;

import com.hoaug.movieapi.common.validator.ValidSafeString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateSearchHistoryRequest {

  @NotBlank(message = "Search keyword is required")
  @Size(min = 1, max = 255, message = "Keyword must be between 1 and 255 characters")
  @ValidSafeString(minLength = 1, maxLength = 255)
  private String keyword;

  public String getKeyword () {
    return keyword;
  }

  public void setKeyword (String keyword) {
    this.keyword = keyword;
  }
}