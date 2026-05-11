package com.hoaug.movieapi.modules.activitylog.infrastructure.persistence.entity;

import com.hoaug.movieapi.common.model.BaseEntity;
import com.hoaug.movieapi.modules.activitylog.domain.model.ActivityScope;
import com.hoaug.movieapi.modules.activitylog.domain.model.ActivitySeverity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "activity_logs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLogEntity extends BaseEntity {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private ActivityScope scope;

  private Long actorId;

  @Column(length = 255)
  private String actorName;

  @Column(nullable = false, length = 100)
  private String action;

  @Column(length = 100)
  private String targetType;

  @Column(length = 100)
  private String targetId;

  @Column(length = 255)
  private String targetLabel;

  @Column(nullable = false, length = 1000)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private ActivitySeverity severity;

  @Column(columnDefinition = "json")
  private String metadata;

  @Column(length = 64)
  private String ipAddress;

  @Column(length = 500)
  private String userAgent;
}
