package com.hoaug.movieapi.modules.activitylog.presentation.rest.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.dto.PageResponse;
import com.hoaug.movieapi.modules.activitylog.application.dto.response.ActivityLogResponse;
import com.hoaug.movieapi.modules.activitylog.domain.model.ActivityScope;
import com.hoaug.movieapi.modules.activitylog.domain.model.ActivitySeverity;
import com.hoaug.movieapi.modules.activitylog.infrastructure.persistence.entity.ActivityLogEntity;
import com.hoaug.movieapi.modules.activitylog.infrastructure.persistence.repository.JpaActivityLogRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix:/api/v1}/admin/activities")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
public class AdminActivityLogController {

  private final JpaActivityLogRepository activityLogRepository;

  @GetMapping
  public PageResponse<ActivityLogResponse> getActivities(
      @RequestParam(value = "scope", required = false) ActivityScope scope,
      @RequestParam(value = "severity", required = false) ActivitySeverity severity,
      @RequestParam(value = "actorId", required = false) Long actorId,
      @RequestParam(value = "actorName", required = false) String actorName,
      @RequestParam(value = "action", required = false) String action,
      @RequestParam(value = "search", required = false) String search,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size) {

    int safePage = Math.max(0, page);
    int safeSize = Math.min(Math.max(1, size), 100);

    PageRequest pageRequest = PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "createdAt"));

    Page<ActivityLogEntity> result = activityLogRepository.searchActivities(
        scope, severity, actorId, actorName, action, search, pageRequest);

    return PageResponse.<ActivityLogResponse>builder()
        .content(result.getContent().stream().map(this::toResponse).toList())
        .totalPages(result.getTotalPages())
        .totalElements(result.getTotalElements())
        .currentPage(result.getNumber())
        .pageSize(result.getSize())
        .hasNext(result.hasNext())
        .build();
  }

  private ActivityLogResponse toResponse(ActivityLogEntity entity) {
    return ActivityLogResponse.builder()
        .id(entity.getId())
        .scope(entity.getScope().name())
        .actorId(entity.getActorId())
        .actorName(entity.getActorName())
        .action(entity.getAction())
        .targetType(entity.getTargetType())
        .targetId(entity.getTargetId())
        .targetLabel(entity.getTargetLabel())
        .description(entity.getDescription())
        .severity(entity.getSeverity().name())
        .ipAddress(entity.getIpAddress())
        .userAgent(entity.getUserAgent())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }
}
