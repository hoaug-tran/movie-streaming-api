package com.hoaug.movieapi.modules.review.presentation.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.modules.review.application.dto.response.ReviewResponse;
import com.hoaug.movieapi.modules.review.application.usecase.GetMovieReviewsUseCase;
import com.hoaug.movieapi.modules.review.domain.model.ReviewStatus;
import com.hoaug.movieapi.modules.review.domain.repository.ReviewRepository;

@RestController
@RequestMapping("${api.prefix:/api/v1}/admin/reviews")
@PreAuthorize("hasRole('ADMIN')")
public class AdminReviewController {

  private final ReviewRepository reviewRepository;
  private final GetMovieReviewsUseCase getMovieReviewsUseCase;

  public AdminReviewController(ReviewRepository reviewRepository,
      GetMovieReviewsUseCase getMovieReviewsUseCase) {
    this.reviewRepository = reviewRepository;
    this.getMovieReviewsUseCase = getMovieReviewsUseCase;
  }

  @GetMapping("/movie/{movieId}")
  public List<ReviewResponse> getMovieReviews (@PathVariable Long movieId) {
    return getMovieReviewsUseCase.execute(movieId);
  }

  @PutMapping("/{id}/status")
  public void updateReviewStatus (@PathVariable Long id, @RequestParam ReviewStatus status) {
    var review = reviewRepository.findById(id)
        .orElseThrow( () -> new RuntimeException("Review not found"));
    review.setStatus(status);
    reviewRepository.save(review);
  }

  @DeleteMapping("/{id}")
  public void deleteReview (@PathVariable Long id) {
    var review = reviewRepository.findById(id)
        .orElseThrow( () -> new RuntimeException("Review not found"));
    reviewRepository.delete(review);
  }
}
