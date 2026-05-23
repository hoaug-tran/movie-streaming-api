package com.hoaug.movieapi.modules.activitylog.application.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLogResponse {
  private Long id;
  private String scope;
  private Long actorId;
  private String actorName;
  private String action;
  private String targetType;
  private String targetId;
  private String targetLabel;
  private String description;
  private String severity;
  private String ipAddress;
  private String userAgent;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
