package com.hoaug.movieapi.modules.report.domain.repository;

import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.report.domain.model.Report;

public interface ReportRepository {

  Optional<Report> findById (Long id);

  Report save (Report report);

  List<Report> findByReporterUserIdOrderByCreatedAtDesc (Long reporterUserId);

  List<Report> findAllByOrderByCreatedAtDesc ();
}