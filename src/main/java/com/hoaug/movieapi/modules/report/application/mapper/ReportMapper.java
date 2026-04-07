package com.hoaug.movieapi.modules.report.application.mapper;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.report.application.dto.response.ReportResponse;
import com.hoaug.movieapi.modules.report.domain.model.Report;

@Component
public class ReportMapper {

  public ReportResponse toResponse (Report report) {
    ReportResponse response = new ReportResponse();
    response.setId(report.getId());
    response.setReporterUserId(report.getReporterUserId());
    response.setCommentId(report.getCommentId());
    response.setReviewId(report.getReviewId());
    response.setReason(report.getReason());
    response.setDescription(report.getDescription());
    response.setStatus(report.getStatus().name());
    response.setCreatedAt(report.getCreatedAt());
    response.setResolvedAt(report.getResolvedAt());
    return response;
  }
}