package com.hoaug.movieapi.modules.movie.application.usecase;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.AdminEpisodeListItemResponse;
import com.hoaug.movieapi.modules.movie.domain.model.Episode;
import com.hoaug.movieapi.modules.movie.domain.model.Movie;
import com.hoaug.movieapi.modules.movie.domain.repository.EpisodeRepository;
import com.hoaug.movieapi.modules.movie.domain.repository.MovieRepository;
import com.hoaug.movieapi.shared.media.MediaUrlResolver;

/**
 * Trả về toàn bộ episode kèm thông tin tóm tắt phim cha cho trang quản trị.
 * Dùng batch lookup theo movieId để tránh N+1 queries.
 */
@Component
public class GetAdminEpisodesUseCase {

  private final EpisodeRepository episodeRepository;
  private final MovieRepository movieRepository;
  private final MediaUrlResolver mediaUrlResolver;

  public GetAdminEpisodesUseCase(EpisodeRepository episodeRepository,
      MovieRepository movieRepository, MediaUrlResolver mediaUrlResolver) {
    this.episodeRepository = episodeRepository;
    this.movieRepository = movieRepository;
    this.mediaUrlResolver = mediaUrlResolver;
  }

  public List<AdminEpisodeListItemResponse> execute () {
    List<Episode> episodes = episodeRepository.findAll();
    if (episodes.isEmpty()) return List.of();

    List<Long> movieIds = episodes.stream().map(Episode::getMovieId).filter(java.util.Objects::nonNull)
        .distinct().toList();
    Map<Long, Movie> moviesById = movieIds.stream()
        .map(movieRepository::findById)
        .flatMap(java.util.Optional::stream)
        .collect(Collectors.toMap(Movie::getId, Function.identity(), (a, b) -> a));

    return episodes.stream().map(ep -> toItem(ep, moviesById.get(ep.getMovieId())))
        .sorted((a, b) -> {
          int cmp = Long.compare(safeLong(b.getMovieId()), safeLong(a.getMovieId()));
          if (cmp != 0) return cmp;
          return Integer.compare(safeInt(a.getEpisodeNumber()), safeInt(b.getEpisodeNumber()));
        }).toList();
  }

  private long safeLong (Long v) {
    return v == null ? 0L : v;
  }

  private int safeInt (Integer v) {
    return v == null ? 0 : v;
  }

  private AdminEpisodeListItemResponse toItem (Episode episode, Movie movie) {
    AdminEpisodeListItemResponse item = new AdminEpisodeListItemResponse();
    item.setId(episode.getId());
    item.setTitle(episode.getTitle());
    item.setEpisodeNumber(episode.getEpisodeNumber());
    item.setVideoUrl(mediaUrlResolver.resolve(episode.getVideoUrl()));
    item.setThumbnailUrl(mediaUrlResolver.resolve(episode.getThumbnailUrl()));
    item.setDurationSeconds(episode.getDurationSeconds());
    item.setIsFreePreview(episode.getIsFreePreview());
    item.setStatus(episode.getStatus());
    String q = episode.getAvailableQualities();
    item.setAvailableQualities(q != null && !q.isBlank() ? List.of(q.split(",")) : List.of());
    item.setCreatedAt(episode.getCreatedAt());
    item.setUpdatedAt(episode.getUpdatedAt());

    item.setMovieId(episode.getMovieId());
    if (movie != null) {
      item.setMovieTitle(movie.getTitle());
      item.setMovieSlug(movie.getSlug());
      item.setMovieType(movie.getMovieType());
      item.setMovieReleaseYear(movie.getReleaseYear());
      item.setMovieCountry(movie.getCountry());
      item.setMovieStatus(movie.getMovieStatus() != null ? movie.getMovieStatus().name() : null);
      item.setMoviePosterUrl(mediaUrlResolver.resolve(movie.getPosterUrl()));
    }
    return item;
  }
}
