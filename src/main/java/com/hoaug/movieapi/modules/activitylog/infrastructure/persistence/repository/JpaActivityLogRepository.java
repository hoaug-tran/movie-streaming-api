package com.hoaug.movieapi.modules.activitylog.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hoaug.movieapi.modules.activitylog.domain.model.ActivityScope;
import com.hoaug.movieapi.modules.activitylog.domain.model.ActivitySeverity;
import com.hoaug.movieapi.modules.activitylog.infrastructure.persistence.entity.ActivityLogEntity;

public interface JpaActivityLogRepository extends JpaRepository<ActivityLogEntity, Long> {
  
  List<ActivityLogEntity> findByScopeOrderByCreatedAtDesc(ActivityScope scope, Pageable pageable);

  @Query("SELECT a FROM ActivityLogEntity a WHERE " +
         "(:scope IS NULL OR a.scope = :scope) AND " +
         "(:severity IS NULL OR a.severity = :severity) AND " +
         "(:actorId IS NULL OR a.actorId = :actorId) AND " +
         "(:actorName IS NULL OR LOWER(a.actorName) LIKE LOWER(CONCAT('%', :actorName, '%'))) AND " +
         "(:action IS NULL OR LOWER(a.action) LIKE LOWER(CONCAT('%', :action, '%'))) AND " +
         "(:search IS NULL OR LOWER(a.actorName) LIKE LOWER(CONCAT('%', :search, '%')) " +
         "  OR LOWER(a.action) LIKE LOWER(CONCAT('%', :search, '%')) " +
         "  OR LOWER(a.targetLabel) LIKE LOWER(CONCAT('%', :search, '%')) " +
         "  OR LOWER(a.description) LIKE LOWER(CONCAT('%', :search, '%')))")
  Page<ActivityLogEntity> searchActivities(
      @Param("scope") ActivityScope scope,
      @Param("severity") ActivitySeverity severity,
      @Param("actorId") Long actorId,
      @Param("actorName") String actorName,
      @Param("action") String action,
      @Param("search") String search,
      Pageable pageable
  );
}
