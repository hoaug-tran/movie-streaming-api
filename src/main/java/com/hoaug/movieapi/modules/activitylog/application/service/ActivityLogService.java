package com.hoaug.movieapi.modules.activitylog.application.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hoaug.movieapi.modules.activitylog.domain.model.ActivityScope;
import com.hoaug.movieapi.modules.activitylog.domain.model.ActivitySeverity;
import com.hoaug.movieapi.modules.activitylog.infrastructure.persistence.entity.ActivityLogEntity;
import com.hoaug.movieapi.modules.activitylog.infrastructure.persistence.repository.JpaActivityLogRepository;
import com.hoaug.movieapi.modules.user.domain.model.Role;
import com.hoaug.movieapi.modules.user.infrastructure.persistence.entity.UserEntity;
import com.hoaug.movieapi.modules.user.infrastructure.persistence.repository.JpaUserRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityLogService {

  private final JpaActivityLogRepository activityLogRepository;
  private final JpaUserRepository userRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void record (ActivityScope scope, Long actorId, String actorName, String action,
      String targetType, Object targetId, String targetLabel, String description,
      ActivitySeverity severity) {

    ActivityScope resolvedScope = scope;
    if (actorId != null) {
      try {
        Optional<UserEntity> userOpt = userRepository.findById(actorId);
        if (userOpt.isPresent() && Role.ROLE_ADMIN.equals(userOpt.get().getRole())) {
          resolvedScope = ActivityScope.ADMIN;
        }
      } catch (Exception e) {
        // ignore
      }
    }

    String ipAddress = null;
    String userAgent = null;

    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
        .getRequestAttributes();
    if (attributes != null) {
      HttpServletRequest request = attributes.getRequest();
      userAgent = request.getHeader("User-Agent");
      ipAddress = request.getHeader("X-Forwarded-For");
      if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
        ipAddress = request.getHeader("X-Real-IP");
      }
      if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
        ipAddress = request.getRemoteAddr();
      }
      if (ipAddress != null && ipAddress.contains(",")) {
        ipAddress = ipAddress.split(",")[0].trim();
      }
    }

    activityLogRepository.save(ActivityLogEntity.builder().scope(resolvedScope).actorId(actorId)
        .actorName(actorName).action(action).targetType(targetType)
        .targetId(targetId == null ? null : String.valueOf(targetId)).targetLabel(targetLabel)
        .description(description).severity(severity == null ? ActivitySeverity.INFO : severity)
        .ipAddress(ipAddress).userAgent(userAgent).build());
  }
}
