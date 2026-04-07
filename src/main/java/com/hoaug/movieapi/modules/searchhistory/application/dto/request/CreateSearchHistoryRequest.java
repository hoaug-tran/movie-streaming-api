package com.hoaug.movieapi.modules.searchhistory.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateSearchHistoryRequest {

  @NotBlank
  @Size(max = 255)
  private String keyword;

  public String getKeyword () {
    return keyword;
  }

  public void setKeyword (String keyword) {
    this.keyword = keyword;
  }
}