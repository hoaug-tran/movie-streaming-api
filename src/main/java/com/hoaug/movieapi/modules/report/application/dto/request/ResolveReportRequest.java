package com.hoaug.movieapi.modules.report.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ResolveReportRequest {

  @NotBlank
  private String status;

  public String getStatus () {
    return status;
  }

  public void setStatus (String status) {
    this.status = status;
  }
}