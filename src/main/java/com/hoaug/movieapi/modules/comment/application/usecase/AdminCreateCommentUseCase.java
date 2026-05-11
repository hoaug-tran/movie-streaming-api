package com.hoaug.movieapi.modules.comment.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.comment.application.dto.request.AdminCommentRequest;
import com.hoaug.movieapi.modules.comment.application.dto.response.CommentResponse;
import com.hoaug.movieapi.modules.comment.application.mapper.CommentMapper;
import com.hoaug.movieapi.modules.comment.domain.model.Comment;
import com.hoaug.movieapi.modules.comment.domain.repository.CommentRepository;
import com.hoaug.movieapi.modules.movie.domain.repository.EpisodeRepository;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;
import com.hoaug.movieapi.modules.user.domain.repository.UserRepository;

import jakarta.transaction.Transactional;

@Component
public class AdminCreateCommentUseCase {

  private final CommentRepository commentRepository;
  private final MovieRepository movieRepository;
  private final EpisodeRepository episodeRepository;
  private final UserRepository userRepository;
  private final CommentMapper commentMapper;

  public AdminCreateCommentUseCase(CommentRepository commentRepository, MovieRepository movieRepository,
      EpisodeRepository episodeRepository, UserRepository userRepository, CommentMapper commentMapper) {
    this.commentRepository = commentRepository;
    this.movieRepository = movieRepository;
    this.episodeRepository = episodeRepository;
    this.userRepository = userRepository;
    this.commentMapper = commentMapper;
  }

  @Transactional
  public CommentResponse execute (AdminCommentRequest request) {
    validateReferences(request, null);

    Comment comment = new Comment();
    comment.setUserId(request.getUserId());
    comment.setMovieId(request.getMovieId());
    comment.setEpisodeId(request.getEpisodeId());
    comment.setParentCommentId(request.getParentCommentId());
    comment.setContent(request.getContent().trim());
    comment.setLikeCount(0);
    comment.setReplyCount(0);
    comment.setStatus(request.getStatus());
    comment.setCreatedAt(LocalDateTime.now());
    comment.setUpdatedAt(LocalDateTime.now());

    Comment saved = commentRepository.save(comment);
    if (saved.getParentCommentId() != null) {
      commentRepository.increaseReplyCount(saved.getParentCommentId());
    }
    return commentMapper.toResponse(saved);
  }

  private void validateReferences (AdminCommentRequest request, Long currentCommentId) {
    userRepository.findById(request.getUserId())
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));
    movieRepository.findById(request.getMovieId())
        .orElseThrow( () -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

    if (request.getEpisodeId() != null) {
      boolean episodeBelongsToMovie = episodeRepository.findPublishedByMovieId(request.getMovieId())
          .stream().anyMatch(episode -> episode.getId().equals(request.getEpisodeId()));
      if (!episodeBelongsToMovie) throw new AppException(ErrorCode.EPISODE_NOT_FOUND);
    }

    if (request.getParentCommentId() != null) {
      if (request.getParentCommentId().equals(currentCommentId)) {
        throw new AppException(ErrorCode.INVALID_COMMENT_PARENT);
      }
      Comment parent = commentRepository.findById(request.getParentCommentId())
          .orElseThrow( () -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
      if (!parent.getMovieId().equals(request.getMovieId())) {
        throw new AppException(ErrorCode.INVALID_COMMENT_PARENT);
      }
    }
  }
}
