package com.hoaug.movieapi.modules.movie.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.domain.model.Episode;
import com.hoaug.movieapi.modules.movie.domain.model.EpisodeStatus;
import com.hoaug.movieapi.modules.movie.domain.repository.EpisodeRepository;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.EpisodeEntity;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaEpisodeRepository;

@Component
public class EpisodeRepositoryAdapter implements EpisodeRepository {

  private final JpaEpisodeRepository jpaEpisodeRepository;

  public EpisodeRepositoryAdapter(JpaEpisodeRepository jpaEpisodeRepository) {
    this.jpaEpisodeRepository = jpaEpisodeRepository;
  }

  @Override
  public List<Episode> findPublishedByMovieId (Long movieId) {
    return jpaEpisodeRepository
        .findByMovieIdAndStatusOrderByEpisodeNumberAsc(movieId, EpisodeStatus.PUBLISHED).stream()
        .map(this::toDomain).toList();
  }

  @Override
  public List<Episode> findAllByMovieId (Long movieId) {
    return jpaEpisodeRepository
        .findByMovieIdOrderByEpisodeNumberAsc(movieId).stream()
        .map(this::toDomain).toList();
  }

  @Override
  public Optional<Episode> findById (Long id) {
    return jpaEpisodeRepository.findById(id).map(this::toDomain);
  }

  @Override
  public Episode save (Episode episode) {
    EpisodeEntity savedEntity = jpaEpisodeRepository.save(toEntity(episode));
    return toDomain(savedEntity);
  }

  private Episode toDomain (EpisodeEntity entity) {
    Episode episode = new Episode();
    episode.setId(entity.getId());
    episode.setMovieId(entity.getMovieId());
    episode.setTitle(entity.getTitle());
    episode.setEpisodeNumber(entity.getEpisodeNumber());
    episode.setVideoUrl(entity.getVideoUrl());
    episode.setThumbnailUrl(entity.getThumbnailUrl());
    episode.setDurationSeconds(entity.getDurationSeconds());
    episode.setIsFreePreview(entity.getIsFreePreview());
    episode.setStatus(entity.getStatus());
    episode.setAvailableQualities(entity.getAvailableQualities());
    episode.setCreatedAt(entity.getCreatedAt());
    episode.setUpdatedAt(entity.getUpdatedAt());
    return episode;
  }

  private EpisodeEntity toEntity (Episode episode) {
    EpisodeEntity entity = new EpisodeEntity();
    entity.setId(episode.getId());
    entity.setMovieId(episode.getMovieId());
    entity.setTitle(episode.getTitle());
    entity.setEpisodeNumber(episode.getEpisodeNumber());
    entity.setVideoUrl(episode.getVideoUrl());
    entity.setThumbnailUrl(episode.getThumbnailUrl());
    entity.setDurationSeconds(episode.getDurationSeconds());
    entity.setIsFreePreview(episode.getIsFreePreview());
    entity.setStatus(episode.getStatus());
    entity.setAvailableQualities(episode.getAvailableQualities());
    entity.setCreatedAt(episode.getCreatedAt());
    entity.setUpdatedAt(episode.getUpdatedAt());
    return entity;
  }

  @Override
  public void deleteById (Long id) {
    jpaEpisodeRepository.deleteById(id);
  }
}