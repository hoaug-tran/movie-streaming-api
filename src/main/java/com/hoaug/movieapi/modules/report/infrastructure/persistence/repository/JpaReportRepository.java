package com.hoaug.movieapi.modules.report.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.report.infrastructure.persistence.entity.ReportEntity;

public interface JpaReportRepository extends JpaRepository<ReportEntity, Long> {

  List<ReportEntity> findByReporterUserIdOrderByCreatedAtDesc (Long reporterUserId);

  List<ReportEntity> findAllByOrderByCreatedAtDesc ();


  long countByStatus (com.hoaug.movieapi.modules.report.domain.model.ReportStatus status);

  long countByCommentIdIsNotNull ();

  long countByReviewIdIsNotNull ();

  long countByCreatedAtAfter (java.time.LocalDateTime createdAt);
}
