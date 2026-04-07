package com.hoaug.movieapi.modules.comment.application.mapper;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.comment.application.dto.response.CommentResponse;
import com.hoaug.movieapi.modules.comment.domain.model.Comment;

@Component
public class CommentMapper {

  public CommentResponse toResponse (Comment comment) {
    CommentResponse response = new CommentResponse();
    response.setId(comment.getId());
    response.setUserId(comment.getUserId());
    response.setMovieId(comment.getMovieId());
    response.setParentCommentId(comment.getParentCommentId());
    response.setContent(comment.getContent());
    response.setLikeCount(comment.getLikeCount());
    response.setReplyCount(comment.getReplyCount());
    response.setStatus(comment.getStatus().name());
    response.setCreatedAt(comment.getCreatedAt());
    response.setUpdatedAt(comment.getUpdatedAt());
    return response;
  }
}