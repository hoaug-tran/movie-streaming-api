package com.hoaug.movieapi.modules.email.domain;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {
  private String to;
  private String subject;
  private String htmlContent;
  private Map<String, String> templateVariables;
  private EmailType emailType;
}
