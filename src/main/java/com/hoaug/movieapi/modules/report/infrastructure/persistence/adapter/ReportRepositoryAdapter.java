package com.hoaug.movieapi.modules.report.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.report.domain.model.Report;
import com.hoaug.movieapi.modules.report.domain.repository.ReportRepository;
import com.hoaug.movieapi.modules.report.infrastructure.persistence.entity.ReportEntity;
import com.hoaug.movieapi.modules.report.infrastructure.persistence.repository.JpaReportRepository;

@Component
public class ReportRepositoryAdapter implements ReportRepository {

  private final JpaReportRepository jpaReportRepository;

  public ReportRepositoryAdapter(JpaReportRepository jpaReportRepository) {
    this.jpaReportRepository = jpaReportRepository;
  }

  @Override
  public Report save (Report report) {
    ReportEntity savedEntity = jpaReportRepository.save(toEntity(report));
    return toDomain(savedEntity);
  }

  @Override
  public List<Report> findByReporterUserIdOrderByCreatedAtDesc (Long reporterUserId) {
    return jpaReportRepository.findByReporterUserIdOrderByCreatedAtDesc(reporterUserId).stream()
        .map(this::toDomain).toList();
  }

  @Override
  public List<Report> findAllByOrderByCreatedAtDesc () {
    return jpaReportRepository.findAllByOrderByCreatedAtDesc().stream().map(this::toDomain)
        .toList();
  }

  @Override
  public Optional<Report> findById (Long id) {
    return jpaReportRepository.findById(id).map(this::toDomain);
  }

  private Report toDomain (ReportEntity entity) {
    Report report = new Report();
    report.setId(entity.getId());
    report.setReporterUserId(entity.getReporterUserId());
    report.setCommentId(entity.getCommentId());
    report.setReviewId(entity.getReviewId());
    report.setReason(entity.getReason());
    report.setDescription(entity.getDescription());
    report.setStatus(entity.getStatus());
    report.setCreatedAt(entity.getCreatedAt());
    report.setResolvedAt(entity.getResolvedAt());
    return report;
  }

  private ReportEntity toEntity (Report report) {
    ReportEntity entity = new ReportEntity();
    entity.setId(report.getId());
    entity.setReporterUserId(report.getReporterUserId());
    entity.setCommentId(report.getCommentId());
    entity.setReviewId(report.getReviewId());
    entity.setReason(report.getReason());
    entity.setDescription(report.getDescription());
    entity.setStatus(report.getStatus());
    entity.setCreatedAt(report.getCreatedAt());
    entity.setResolvedAt(report.getResolvedAt());
    return entity;
  }
}