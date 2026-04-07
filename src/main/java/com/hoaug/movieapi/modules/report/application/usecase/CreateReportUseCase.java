package com.hoaug.movieapi.modules.report.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.comment.domain.repository.CommentRepository;
import com.hoaug.movieapi.modules.report.application.dto.request.CreateReportRequest;
import com.hoaug.movieapi.modules.report.application.dto.response.ReportResponse;
import com.hoaug.movieapi.modules.report.application.mapper.ReportMapper;
import com.hoaug.movieapi.modules.report.domain.model.Report;
import com.hoaug.movieapi.modules.report.domain.model.ReportStatus;
import com.hoaug.movieapi.modules.report.domain.repository.ReportRepository;
import com.hoaug.movieapi.modules.review.domain.repository.ReviewRepository;

@Component
public class CreateReportUseCase {

  private final ReportRepository reportRepository;
  private final CommentRepository commentRepository;
  private final ReviewRepository reviewRepository;
  private final ReportMapper reportMapper;

  public CreateReportUseCase(ReportRepository reportRepository, CommentRepository commentRepository,
      ReviewRepository reviewRepository, ReportMapper reportMapper) {
    this.reportRepository = reportRepository;
    this.commentRepository = commentRepository;
    this.reviewRepository = reviewRepository;
    this.reportMapper = reportMapper;
  }

  public ReportResponse execute (Long reporterUserId, CreateReportRequest request) {
    boolean hasComment = request.getCommentId() != null;
    boolean hasReview = request.getReviewId() != null;

    if (hasComment == hasReview) {
      throw new AppException(ErrorCode.INVALID_REPORT_TARGET);
    }

    if (hasComment) {
      commentRepository.findById(request.getCommentId())
          .orElseThrow( () -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
    }

    if (hasReview) {
      reviewRepository.findById(request.getReviewId())
          .orElseThrow( () -> new AppException(ErrorCode.REVIEW_NOT_FOUND));
    }

    Report report = new Report();
    report.setReporterUserId(reporterUserId);
    report.setCommentId(request.getCommentId());
    report.setReviewId(request.getReviewId());
    report.setReason(request.getReason());
    report.setDescription(request.getDescription());
    report.setStatus(ReportStatus.PENDING);
    report.setCreatedAt(LocalDateTime.now());

    Report savedReport = reportRepository.save(report);
    return reportMapper.toResponse(savedReport);
  }
}