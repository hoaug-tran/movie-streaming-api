package com.hoaug.movieapi.modules.activitylog.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hoaug.movieapi.modules.activitylog.domain.model.ActivityScope;
import com.hoaug.movieapi.modules.activitylog.domain.model.ActivitySeverity;
import com.hoaug.movieapi.modules.activitylog.infrastructure.persistence.entity.ActivityLogEntity;
import com.hoaug.movieapi.modules.activitylog.infrastructure.persistence.repository.JpaActivityLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityLogService {

  private final JpaActivityLogRepository activityLogRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void record (ActivityScope scope, Long actorId, String actorName, String action,
      String targetType, Object targetId, String targetLabel, String description,
      ActivitySeverity severity) {
    activityLogRepository.save(ActivityLogEntity.builder().scope(scope).actorId(actorId)
        .actorName(actorName).action(action).targetType(targetType)
        .targetId(targetId == null ? null : String.valueOf(targetId)).targetLabel(targetLabel)
        .description(description).severity(severity == null ? ActivitySeverity.INFO : severity).build());
  }
}
