package com.hoaug.movieapi.modules.commentlike.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.event.CommentLikedEvent;
import com.hoaug.movieapi.common.event.EventPublisher;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.common.security.LikeSpamValidator;
import com.hoaug.movieapi.modules.comment.domain.model.Comment;
import com.hoaug.movieapi.modules.comment.domain.repository.CommentRepository;
import com.hoaug.movieapi.modules.commentlike.application.dto.response.CommentLikeResponse;
import com.hoaug.movieapi.modules.commentlike.application.mapper.CommentLikeMapper;
import com.hoaug.movieapi.modules.commentlike.domain.model.CommentLike;
import com.hoaug.movieapi.modules.commentlike.domain.repository.CommentLikeRepository;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;

import jakarta.transaction.Transactional;

@Component
public class ToggleCommentLikeUseCase {

  private final CommentLikeRepository commentLikeRepository;
  private final CommentRepository commentRepository;
  private final MovieRepository movieRepository;
  private final CommentLikeMapper commentLikeMapper;
  private final LikeSpamValidator likeSpamValidator;
  private final EventPublisher eventPublisher;

  public ToggleCommentLikeUseCase(CommentLikeRepository commentLikeRepository,
      CommentRepository commentRepository, MovieRepository movieRepository,
      CommentLikeMapper commentLikeMapper, LikeSpamValidator likeSpamValidator,
      EventPublisher eventPublisher) {
    this.commentLikeRepository = commentLikeRepository;
    this.commentRepository = commentRepository;
    this.movieRepository = movieRepository;
    this.commentLikeMapper = commentLikeMapper;
    this.likeSpamValidator = likeSpamValidator;
    this.eventPublisher = eventPublisher;
  }

  @Transactional
  public CommentLikeResponse execute(Long userId, Long commentId) {
    likeSpamValidator.validateCommentLike(userId, commentId);

    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

    return commentLikeRepository.findByUserIdAndCommentId(userId, commentId).map(existingLike -> {
      commentLikeRepository.delete(existingLike);
      syncCommentLikeCount(commentId);
      return commentLikeMapper.toResponse(commentId, false);
    }).orElseGet(() -> {
      CommentLike commentLike = new CommentLike();
      commentLike.setUserId(userId);
      commentLike.setCommentId(commentId);
      commentLike.setCreatedAt(LocalDateTime.now());
      commentLikeRepository.save(commentLike);
      syncCommentLikeCount(commentId);

      String movieSlug = movieRepository.findById(comment.getMovieId())
          .map(m -> m.getSlug()).orElse(null);

      eventPublisher.publish(new CommentLikedEvent(
          commentId,
          comment.getUserId(),
          userId,
          comment.getMovieId(),
          movieSlug));

      return commentLikeMapper.toResponse(commentId, true);
    });
  }

  private void syncCommentLikeCount(Long commentId) {
    long realLikeCount = commentLikeRepository.countByCommentId(commentId);
    commentRepository.updateLikeCount(commentId, Math.toIntExact(realLikeCount));
  }
}