package com.hoaug.movieapi.modules.commentlike.application.mapper;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.commentlike.application.dto.response.CommentLikeResponse;

@Component
public class CommentLikeMapper {

  public CommentLikeResponse toResponse (Long commentId, boolean liked) {
    CommentLikeResponse response = new CommentLikeResponse();
    response.setCommentId(commentId);
    response.setLiked(liked);
    return response;
  }
}