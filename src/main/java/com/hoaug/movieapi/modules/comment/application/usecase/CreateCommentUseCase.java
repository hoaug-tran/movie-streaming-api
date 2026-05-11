package com.hoaug.movieapi.modules.comment.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.comment.application.dto.request.CreateCommentRequest;
import com.hoaug.movieapi.modules.comment.application.dto.response.CommentResponse;
import com.hoaug.movieapi.modules.comment.application.mapper.CommentMapper;
import com.hoaug.movieapi.modules.comment.application.validator.CommentSpamValidator;
import com.hoaug.movieapi.modules.comment.domain.model.Comment;
import com.hoaug.movieapi.modules.comment.domain.model.CommentStatus;
import com.hoaug.movieapi.modules.comment.domain.repository.CommentRepository;
import com.hoaug.movieapi.modules.movie.domain.repository.EpisodeRepository;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;

import jakarta.transaction.Transactional;

@Component
public class CreateCommentUseCase {

  private final CommentRepository commentRepository;
  private final MovieRepository movieRepository;
  private final EpisodeRepository episodeRepository;
  private final CommentMapper commentMapper;
  private final CommentSpamValidator spamValidator;

  public CreateCommentUseCase(CommentRepository commentRepository, MovieRepository movieRepository,
      EpisodeRepository episodeRepository, CommentMapper commentMapper,
      CommentSpamValidator spamValidator) {
    this.commentRepository = commentRepository;
    this.movieRepository = movieRepository;
    this.episodeRepository = episodeRepository;
    this.commentMapper = commentMapper;
    this.spamValidator = spamValidator;
  }

  @Transactional
  public CommentResponse execute (Long userId, CreateCommentRequest request) {
    spamValidator.validate(userId, request.getContent());

    var movie = movieRepository.findById(request.getMovieId())
        .orElseThrow( () -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

    if (Boolean.TRUE.equals(movie.getCommentsLocked())) {
      throw new AppException(ErrorCode.MOVIE_COMMENTS_LOCKED);
    }

    if (request.getEpisodeId() != null) {
      boolean episodeBelongsToMovie = episodeRepository.findPublishedByMovieId(request.getMovieId())
          .stream().anyMatch(episode -> episode.getId().equals(request.getEpisodeId()));

      if (!episodeBelongsToMovie) {
        throw new AppException(ErrorCode.EPISODE_NOT_FOUND);
      }
    }

    if (request.getParentCommentId() != null) {
      Comment parentComment = commentRepository.findById(request.getParentCommentId())
          .orElseThrow( () -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

      if (!parentComment.getMovieId().equals(request.getMovieId())) {
        throw new AppException(ErrorCode.INVALID_COMMENT_PARENT);
      }
    }

    Comment comment = new Comment();
    comment.setUserId(userId);
    comment.setMovieId(request.getMovieId());
    comment.setEpisodeId(request.getEpisodeId());
    comment.setParentCommentId(request.getParentCommentId());
    comment.setContent(request.getContent());
    comment.setLikeCount(0);
    comment.setReplyCount(0);
    comment.setStatus(CommentStatus.VISIBLE);
    comment.setCreatedAt(LocalDateTime.now());
    comment.setUpdatedAt(LocalDateTime.now());

    Comment savedComment = commentRepository.save(comment);

    if (request.getParentCommentId() != null) {
      commentRepository.increaseReplyCount(request.getParentCommentId());
    }

    return commentMapper.toResponse(savedComment);
  }
}