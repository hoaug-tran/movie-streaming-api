package com.hoaug.movieapi.modules.review.presentation.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.modules.review.application.dto.request.UpsertReviewRequest;
import com.hoaug.movieapi.modules.review.application.dto.response.ReviewResponse;
import com.hoaug.movieapi.modules.review.application.usecase.UpsertReviewUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix:/api/v1}/reviews")
public class ReviewController {

  private final UpsertReviewUseCase useCase;

  public ReviewController(UpsertReviewUseCase useCase) {
    this.useCase = useCase;
  }

  @PostMapping
  public ReviewResponse upsert (Authentication auth, @Valid @RequestBody UpsertReviewRequest req) {
    return useCase.execute(1L, req);
  }
}