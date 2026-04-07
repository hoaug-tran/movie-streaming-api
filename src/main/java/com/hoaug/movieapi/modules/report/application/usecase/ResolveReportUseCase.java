package com.hoaug.movieapi.modules.report.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.report.application.dto.request.ResolveReportRequest;
import com.hoaug.movieapi.modules.report.application.dto.response.ReportResponse;
import com.hoaug.movieapi.modules.report.application.mapper.ReportMapper;
import com.hoaug.movieapi.modules.report.domain.model.Report;
import com.hoaug.movieapi.modules.report.domain.model.ReportStatus;
import com.hoaug.movieapi.modules.report.domain.repository.ReportRepository;

@Component
public class ResolveReportUseCase {

  private final ReportRepository reportRepository;
  private final ReportMapper reportMapper;

  public ResolveReportUseCase(ReportRepository reportRepository, ReportMapper reportMapper) {
    this.reportRepository = reportRepository;
    this.reportMapper = reportMapper;
  }

  public ReportResponse execute (Long reportId, ResolveReportRequest request) {
    Report report = reportRepository.findById(reportId)
        .orElseThrow( () -> new AppException(ErrorCode.REPORT_NOT_FOUND));

    ReportStatus status = ReportStatus.valueOf(request.getStatus());

    if (status == ReportStatus.PENDING) {
      throw new AppException(ErrorCode.INVALID_REPORT_STATUS);
    }

    report.setStatus(status);
    report.setResolvedAt(LocalDateTime.now());

    Report savedReport = reportRepository.save(report);
    return reportMapper.toResponse(savedReport);
  }
}