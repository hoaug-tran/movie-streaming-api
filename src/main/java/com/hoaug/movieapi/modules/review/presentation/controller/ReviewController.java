package com.hoaug.movieapi.modules.review.presentation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.dto.PageResponse;
import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.common.response.ResponseUtil;
import com.hoaug.movieapi.modules.auth.domain.repository.AuthUserRepository;
import com.hoaug.movieapi.modules.review.application.dto.request.UpsertReviewRequest;
import com.hoaug.movieapi.modules.review.application.dto.response.ReviewResponse;
import com.hoaug.movieapi.modules.review.application.usecase.DeleteReviewUseCase;
import com.hoaug.movieapi.modules.review.application.usecase.GetMovieReviewsUseCase;
import com.hoaug.movieapi.modules.review.application.usecase.GetMyReviewsUseCase;
import com.hoaug.movieapi.modules.review.application.usecase.GetReviewByIdUseCase;
import com.hoaug.movieapi.modules.review.application.usecase.UpsertReviewUseCase;
import com.hoaug.movieapi.modules.user.domain.model.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix:/api/v1}/reviews")
public class ReviewController {

  private final UpsertReviewUseCase upsertReviewUseCase;
  private final GetReviewByIdUseCase getReviewByIdUseCase;
  private final GetMovieReviewsUseCase getMovieReviewsUseCase;
  private final GetMyReviewsUseCase getMyReviewsUseCase;
  private final DeleteReviewUseCase deleteReviewUseCase;
  private final AuthUserRepository authUserRepository;

  public ReviewController(UpsertReviewUseCase upsertReviewUseCase,
      GetReviewByIdUseCase getReviewByIdUseCase, GetMovieReviewsUseCase getMovieReviewsUseCase,
      GetMyReviewsUseCase getMyReviewsUseCase, DeleteReviewUseCase deleteReviewUseCase,
      AuthUserRepository authUserRepository) {
    this.upsertReviewUseCase = upsertReviewUseCase;
    this.getReviewByIdUseCase = getReviewByIdUseCase;
    this.getMovieReviewsUseCase = getMovieReviewsUseCase;
    this.getMyReviewsUseCase = getMyReviewsUseCase;
    this.deleteReviewUseCase = deleteReviewUseCase;
    this.authUserRepository = authUserRepository;
  }

  @PostMapping
  public ResponseEntity<ReviewResponse> upsert (Authentication auth,
      @Valid @RequestBody UpsertReviewRequest req) {
    ReviewResponse response = upsertReviewUseCase.execute(getCurrentUserId(auth), req);
    return ResponseUtil.created(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ReviewResponse> getById (@PathVariable Long id) {
    ReviewResponse response = getReviewByIdUseCase.execute(id);
    return ResponseUtil.ok(response);
  }

  @GetMapping("/movie/{movieId}")
  public ResponseEntity<List<ReviewResponse>> getMovieReviews (@PathVariable Long movieId) {
    List<ReviewResponse> reviews = getMovieReviewsUseCase.execute(movieId);
    return ResponseUtil.ok(reviews);
  }

  @GetMapping("/movie/{movieId}/page")
  public ResponseEntity<PageResponse<ReviewResponse>> getMovieReviewsPage (@PathVariable Long movieId,
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    PageResponse<ReviewResponse> reviews = getMovieReviewsUseCase.execute(movieId, page, size);
    return ResponseUtil.ok(reviews);
  }

  @GetMapping("/my-reviews")
  public ResponseEntity<List<ReviewResponse>> getMyReviews (Authentication auth) {
    List<ReviewResponse> reviews = getMyReviewsUseCase.execute(getCurrentUserId(auth));
    return ResponseUtil.ok(reviews);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete (Authentication auth, @PathVariable Long id) {
    deleteReviewUseCase.execute(getCurrentUserId(auth), id);
    return ResponseUtil.noContent();
  }

  private Long getCurrentUserId (Authentication auth) {
    User user = authUserRepository.findByUsername(auth.getName())
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));
    return user.getId();
  }
}