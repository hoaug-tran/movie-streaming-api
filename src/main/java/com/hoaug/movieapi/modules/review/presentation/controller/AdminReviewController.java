package com.hoaug.movieapi.modules.review.presentation.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.modules.review.application.dto.request.UpdateReviewStatusRequest;
import com.hoaug.movieapi.modules.review.application.dto.response.ReviewResponse;
import com.hoaug.movieapi.modules.review.application.usecase.GetMovieReviewsUseCase;
import com.hoaug.movieapi.modules.review.application.usecase.UpdateReviewStatusUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix:/api/v1}/admin/reviews")
@PreAuthorize("hasRole('ADMIN')")
public class AdminReviewController {

  private final GetMovieReviewsUseCase getMovieReviewsUseCase;
  private final UpdateReviewStatusUseCase updateReviewStatusUseCase;

  public AdminReviewController(GetMovieReviewsUseCase getMovieReviewsUseCase,
      UpdateReviewStatusUseCase updateReviewStatusUseCase) {
    this.getMovieReviewsUseCase = getMovieReviewsUseCase;
    this.updateReviewStatusUseCase = updateReviewStatusUseCase;
  }

  @GetMapping("/movie/{movieId}")
  public List<ReviewResponse> getMovieReviews (@PathVariable Long movieId) {
    return getMovieReviewsUseCase.execute(movieId);
  }

  @PatchMapping("/{id}/status")
  public ReviewResponse updateReviewStatus (@PathVariable Long id,
      @Valid @RequestBody UpdateReviewStatusRequest request) {
    return updateReviewStatusUseCase.execute(id, request);
  }
}
