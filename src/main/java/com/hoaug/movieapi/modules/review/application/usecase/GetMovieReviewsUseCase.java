package com.hoaug.movieapi.modules.review.application.usecase;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.dto.PageResponse;
import com.hoaug.movieapi.modules.review.application.dto.response.ReviewResponse;
import com.hoaug.movieapi.modules.review.application.mapper.ReviewMapper;
import com.hoaug.movieapi.modules.review.domain.model.Review;
import com.hoaug.movieapi.modules.review.domain.repository.ReviewRepository;

@Component
public class GetMovieReviewsUseCase {

  private final ReviewRepository reviewRepository;
  private final ReviewMapper reviewMapper;

  public GetMovieReviewsUseCase(ReviewRepository reviewRepository, ReviewMapper reviewMapper) {
    this.reviewRepository = reviewRepository;
    this.reviewMapper = reviewMapper;
  }

  public List<ReviewResponse> execute (Long movieId) {
    return reviewRepository.findByMovieIdOrderByCreatedAtDesc(movieId).stream()
        .map(reviewMapper::toResponse).toList();
  }

  public PageResponse<ReviewResponse> execute (Long movieId, int page, int size) {
    int safePage = Math.max(0, page);
    int safeSize = Math.min(Math.max(1, size), 50);
    PageRequest pageRequest = PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC,
        "createdAt"));
    Page<Review> result = reviewRepository.findByMovieIdOrderByCreatedAtDesc(movieId,
        pageRequest);
    return PageResponse.<ReviewResponse>builder()
        .content(result.getContent().stream().map(reviewMapper::toResponse).toList())
        .totalPages(result.getTotalPages())
        .totalElements(result.getTotalElements())
        .currentPage(result.getNumber())
        .pageSize(result.getSize())
        .hasNext(result.hasNext())
        .build();
  }
}
