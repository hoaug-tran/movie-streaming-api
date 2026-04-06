package com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.movie.domain.model.EpisodeStatus;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.EpisodeEntity;

public interface JpaEpisodeRepository extends JpaRepository<EpisodeEntity, Long> {
  List<EpisodeEntity> findByMovieIdAndStatusOrderByEpisodeNumberAsc (Long movieId,
      EpisodeStatus status);
}