package com.hoaug.movieapi.modules.report.application.dto.request;

import com.hoaug.movieapi.common.validator.ValidSafeString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResolveReportRequest {

  @NotBlank(message = "Report resolution status is required")
  @Size(min = 1, max = 50, message = "Status must be between 1 and 50 characters")
  @ValidSafeString(minLength = 1, maxLength = 50)
  private String status;

  public String getStatus () {
    return status;
  }

  public void setStatus (String status) {
    this.status = status;
  }

}