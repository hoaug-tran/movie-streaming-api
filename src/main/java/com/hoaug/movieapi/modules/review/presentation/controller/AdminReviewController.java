package com.hoaug.movieapi.modules.review.presentation.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.common.response.ResponseUtil;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.MovieEntity;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaMovieRepository;
import com.hoaug.movieapi.modules.review.application.dto.request.UpdateReviewStatusRequest;
import com.hoaug.movieapi.modules.review.application.dto.response.AdminReviewResponse;
import com.hoaug.movieapi.modules.review.application.dto.response.ReviewResponse;
import com.hoaug.movieapi.modules.review.application.usecase.GetMovieReviewsUseCase;
import com.hoaug.movieapi.modules.review.application.usecase.UpdateReviewStatusUseCase;
import com.hoaug.movieapi.modules.review.domain.model.Review;
import com.hoaug.movieapi.modules.review.domain.repository.ReviewRepository;
import com.hoaug.movieapi.modules.user.infrastructure.persistence.entity.UserEntity;
import com.hoaug.movieapi.modules.user.infrastructure.persistence.repository.JpaUserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix:/api/v1}/admin/reviews")
public class AdminReviewController {

  private final ReviewRepository reviewRepository;
  private final JpaUserRepository jpaUserRepository;
  private final JpaMovieRepository jpaMovieRepository;
  private final GetMovieReviewsUseCase getMovieReviewsUseCase;
  private final UpdateReviewStatusUseCase updateReviewStatusUseCase;

  public AdminReviewController(ReviewRepository reviewRepository,
      JpaUserRepository jpaUserRepository,
      JpaMovieRepository jpaMovieRepository,
      GetMovieReviewsUseCase getMovieReviewsUseCase,
      UpdateReviewStatusUseCase updateReviewStatusUseCase) {
    this.reviewRepository = reviewRepository;
    this.jpaUserRepository = jpaUserRepository;
    this.jpaMovieRepository = jpaMovieRepository;
    this.getMovieReviewsUseCase = getMovieReviewsUseCase;
    this.updateReviewStatusUseCase = updateReviewStatusUseCase;
  }

  @GetMapping("")
  @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
  public ResponseEntity<List<AdminReviewResponse>> getAllReviews () {
    List<Review> reviews = reviewRepository.findAll();

    // Batch fetch users and movies to avoid N+1
    Set<Long> userIds = reviews.stream()
        .filter(r -> r.getUserId() != null)
        .map(Review::getUserId)
        .collect(Collectors.toSet());
    Set<Long> movieIds = reviews.stream()
        .filter(r -> r.getMovieId() != null)
        .map(Review::getMovieId)
        .collect(Collectors.toSet());

    Map<Long, UserEntity> userMap = jpaUserRepository.findAllById(userIds).stream()
        .collect(Collectors.toMap(u -> u.getId(), u -> u));
    Map<Long, MovieEntity> movieMap = jpaMovieRepository.findAllById(movieIds).stream()
        .collect(Collectors.toMap(m -> m.getId(), m -> m));

    List<AdminReviewResponse> result = reviews.stream().map(r -> {
      AdminReviewResponse res = new AdminReviewResponse();
      res.setId(r.getId());
      res.setUserId(r.getUserId());
      res.setMovieId(r.getMovieId());
      res.setRating(r.getRating());
      res.setTitle(r.getTitle());
      res.setContent(r.getContent());
      res.setIsEdited(r.getIsEdited());
      res.setStatus(r.getStatus());
      res.setLikeCount(r.getLikeCount());
      res.setCreatedAt(r.getCreatedAt());
      res.setUpdatedAt(r.getUpdatedAt());

      if (r.getUserId() != null) {
        UserEntity u = userMap.get(r.getUserId());
        if (u != null) {
          res.setAuthorUsername(u.getUsername());
          res.setAuthorFullName(u.getFullName());
          res.setAuthorAvatarUrl(u.getAvatarUrl());
        }
      }

      if (r.getMovieId() != null) {
        MovieEntity m = movieMap.get(r.getMovieId());
        if (m != null) {
          res.setMovieTitle(m.getTitle());
          res.setMovieSlug(m.getSlug());
        }
      }

      return res;
    }).toList();

    return ResponseUtil.ok(result);
  }

  @GetMapping("/movie/{movieId}")
  @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
  public List<ReviewResponse> getMovieReviews (@PathVariable Long movieId) {
    return getMovieReviewsUseCase.execute(movieId);
  }

  @PatchMapping("/{id}/status")
  @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
  public ReviewResponse updateReviewStatus (@PathVariable Long id,
      @Valid @RequestBody UpdateReviewStatusRequest request) {
    return updateReviewStatusUseCase.execute(id, request);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteReview (@PathVariable Long id) {
    var review = reviewRepository.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));
    reviewRepository.delete(review);
    return ResponseUtil.noContent();
  }
}
