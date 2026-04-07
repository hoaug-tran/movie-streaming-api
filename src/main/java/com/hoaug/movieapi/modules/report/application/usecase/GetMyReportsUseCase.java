package com.hoaug.movieapi.modules.report.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.report.application.dto.response.ReportResponse;
import com.hoaug.movieapi.modules.report.application.mapper.ReportMapper;
import com.hoaug.movieapi.modules.report.domain.repository.ReportRepository;

@Component
public class GetMyReportsUseCase {

  private final ReportRepository reportRepository;
  private final ReportMapper reportMapper;

  public GetMyReportsUseCase(ReportRepository reportRepository, ReportMapper reportMapper) {
    this.reportRepository = reportRepository;
    this.reportMapper = reportMapper;
  }

  public List<ReportResponse> execute (Long reporterUserId) {
    return reportRepository.findByReporterUserIdOrderByCreatedAtDesc(reporterUserId).stream()
        .map(reportMapper::toResponse).toList();
  }
}