package com.hoaug.movieapi.modules.reviewlike.application.mapper;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.reviewlike.application.dto.response.ReviewLikeResponse;

@Component
public class ReviewLikeMapper {

  public ReviewLikeResponse toResponse (Long reviewId, boolean liked) {
    ReviewLikeResponse response = new ReviewLikeResponse();
    response.setReviewId(reviewId);
    response.setLiked(liked);
    return response;
  }
}