package com.hoaug.movieapi.modules.activitylog.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.activitylog.domain.model.ActivityScope;
import com.hoaug.movieapi.modules.activitylog.infrastructure.persistence.entity.ActivityLogEntity;

public interface JpaActivityLogRepository extends JpaRepository<ActivityLogEntity, Long> {
  List<ActivityLogEntity> findByScopeOrderByCreatedAtDesc (ActivityScope scope, Pageable pageable);
}
