package com.hoaug.movieapi.modules.movie.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.EpisodeResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.MovieMapper;
import com.hoaug.movieapi.modules.movie.domain.repository.EpisodeRepository;

@Component
public class GetEpisodesByMovieUseCase {

  private final EpisodeRepository episodeRepository;
  private final MovieMapper movieMapper;

  public GetEpisodesByMovieUseCase(EpisodeRepository episodeRepository, MovieMapper movieMapper) {
    this.episodeRepository = episodeRepository;
    this.movieMapper = movieMapper;
  }

  public List<EpisodeResponse> execute (Long movieId) {
    return episodeRepository.findPublishedByMovieId(movieId).stream()
        .map(movieMapper::toEpisodeResponse).toList();
  }
}