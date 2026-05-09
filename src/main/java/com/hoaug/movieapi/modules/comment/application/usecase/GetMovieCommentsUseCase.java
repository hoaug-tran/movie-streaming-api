package com.hoaug.movieapi.modules.comment.application.usecase;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.dto.PageResponse;
import com.hoaug.movieapi.modules.comment.application.dto.response.CommentResponse;
import com.hoaug.movieapi.modules.comment.application.mapper.CommentMapper;
import com.hoaug.movieapi.modules.comment.domain.model.Comment;
import com.hoaug.movieapi.modules.comment.domain.repository.CommentRepository;
import com.hoaug.movieapi.modules.commentlike.domain.repository.CommentLikeRepository;

@Component
public class GetMovieCommentsUseCase {

  private final CommentRepository commentRepository;
  private final CommentLikeRepository commentLikeRepository;
  private final CommentMapper commentMapper;

  public GetMovieCommentsUseCase(CommentRepository commentRepository,
      CommentLikeRepository commentLikeRepository, CommentMapper commentMapper) {
    this.commentRepository = commentRepository;
    this.commentLikeRepository = commentLikeRepository;
    this.commentMapper = commentMapper;
  }

  public List<CommentResponse> execute (Long movieId, Long currentUserId) {
    return commentRepository.findVisibleCommentsByMovieIdOrderByCreatedAtDesc(movieId).stream()
        .map(comment -> toResponse(comment, currentUserId)).toList();
  }

  public PageResponse<CommentResponse> execute (Long movieId, Long currentUserId, int page,
      int size) {
    int safePage = Math.max(0, page);
    int safeSize = Math.min(Math.max(1, size), 50);
    PageRequest pageRequest = PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC,
        "createdAt"));
    Page<Comment> result = commentRepository.findVisibleRootCommentsByMovieIdOrderByCreatedAtDesc(
        movieId, pageRequest);
    return PageResponse.<CommentResponse>builder()
        .content(result.getContent().stream().map(comment -> toResponse(comment, currentUserId))
            .toList())
        .totalPages(result.getTotalPages())
        .totalElements(result.getTotalElements())
        .currentPage(result.getNumber())
        .pageSize(result.getSize())
        .hasNext(result.hasNext())
        .build();
  }

  private CommentResponse toResponse (Comment comment, Long currentUserId) {
    CommentResponse response = commentMapper.toResponse(comment);
    long realLikeCount = commentLikeRepository.countByCommentId(comment.getId());
    response.setLikeCount(Math.toIntExact(realLikeCount));
    response.setLikedByCurrentUser(currentUserId != null
        && commentLikeRepository.existsByUserIdAndCommentId(currentUserId, comment.getId()));
    return response;
  }
}