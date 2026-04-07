package com.hoaug.movieapi.modules.report.presentation.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.report.application.dto.request.CreateReportRequest;
import com.hoaug.movieapi.modules.report.application.dto.request.ResolveReportRequest;
import com.hoaug.movieapi.modules.report.application.dto.response.ReportResponse;
import com.hoaug.movieapi.modules.report.application.usecase.CreateReportUseCase;
import com.hoaug.movieapi.modules.report.application.usecase.GetAllReportsUseCase;
import com.hoaug.movieapi.modules.report.application.usecase.GetMyReportsUseCase;
import com.hoaug.movieapi.modules.report.application.usecase.ResolveReportUseCase;
import com.hoaug.movieapi.modules.user.domain.model.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix:/api/v1}/reports")
public class ReportController {

  private final CreateReportUseCase createReportUseCase;
  private final GetMyReportsUseCase getMyReportsUseCase;
  private final GetAllReportsUseCase getAllReportsUseCase;
  private final ResolveReportUseCase resolveReportUseCase;
  private final AuthUserRepository authUserRepository;

  public ReportController(CreateReportUseCase createReportUseCase,
      GetMyReportsUseCase getMyReportsUseCase, GetAllReportsUseCase getAllReportsUseCase,
      ResolveReportUseCase resolveReportUseCase, AuthUserRepository authUserRepository) {
    this.createReportUseCase = createReportUseCase;
    this.getMyReportsUseCase = getMyReportsUseCase;
    this.getAllReportsUseCase = getAllReportsUseCase;
    this.resolveReportUseCase = resolveReportUseCase;
    this.authUserRepository = authUserRepository;
  }

  @PostMapping
  public ReportResponse create (Authentication authentication,
      @Valid @RequestBody CreateReportRequest request) {
    return createReportUseCase.execute(getCurrentUserId(authentication), request);
  }

  @GetMapping("/me")
  public List<ReportResponse> getMyReports (Authentication authentication) {
    return getMyReportsUseCase.execute(getCurrentUserId(authentication));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public List<ReportResponse> getAllReports () {
    return getAllReportsUseCase.execute();
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PatchMapping("/{reportId}/resolve")
  public ReportResponse resolve (@PathVariable Long reportId,
      @Valid @RequestBody ResolveReportRequest request) {
    return resolveReportUseCase.execute(reportId, request);
  }

  private Long getCurrentUserId (Authentication authentication) {
    User user = authUserRepository.findByUsername(authentication.getName())
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));
    return user.getId();
  }
}