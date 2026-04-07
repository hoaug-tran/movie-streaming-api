package com.hoaug.movieapi.modules.review.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.review.application.dto.request.UpsertReviewRequest;
import com.hoaug.movieapi.modules.review.application.dto.response.ReviewResponse;
import com.hoaug.movieapi.modules.review.application.mapper.ReviewMapper;
import com.hoaug.movieapi.modules.review.domain.model.Review;
import com.hoaug.movieapi.modules.review.domain.repository.ReviewRepository;

@Component
public class UpsertReviewUseCase {

  private final ReviewRepository repo;
  private final ReviewMapper mapper;

  public UpsertReviewUseCase(ReviewRepository repo, ReviewMapper mapper) {
    this.repo = repo;
    this.mapper = mapper;
  }

  public ReviewResponse execute (Long userId, UpsertReviewRequest req) {

    Review review = repo.findByUserIdAndMovieId(userId, req.getMovieId()).orElseGet(Review::new);

    if (review.getId() == null) {
      review.setUserId(userId);
      review.setCreatedAt(LocalDateTime.now());
    }

    review.setMovieId(req.getMovieId());
    review.setRating(req.getRating());
    review.setContent(req.getContent());
    review.setUpdatedAt(LocalDateTime.now());

    return mapper.toResponse(repo.save(review));
  }
}