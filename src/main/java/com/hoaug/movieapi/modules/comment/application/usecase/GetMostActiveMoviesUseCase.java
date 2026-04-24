package com.hoaug.movieapi.modules.comment.application.usecase;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.comment.domain.repository.CommentRepository;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieListResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.MovieMapper;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;

@Component
public class GetMostActiveMoviesUseCase {

  private final CommentRepository commentRepository;
  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;

  public GetMostActiveMoviesUseCase(CommentRepository commentRepository,
      MovieRepository movieRepository, MovieMapper movieMapper) {
    this.commentRepository = commentRepository;
    this.movieRepository = movieRepository;
    this.movieMapper = movieMapper;
  }

  public MovieListResponse execute (int limit) {
    List<Long> movieIds = commentRepository.findMostActiveMovieIds(limit);
    return MovieListResponse.builder()
        .movies(movieIds.stream()
            .map(id -> movieRepository.findPublishedById(id).orElse(null))
            .filter(Objects::nonNull)
            .map(movieMapper::toSummaryResponse).toList())
        .build();
  }
}
