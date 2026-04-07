package com.hoaug.movieapi.modules.favorite.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaug.movieapi.modules.favorite.infrastructure.persistence.entity.FavoriteEntity;

public interface JpaFavoriteRepository extends JpaRepository<FavoriteEntity, Long> {
  Optional<FavoriteEntity> findByUserIdAndMovieId (Long userId, Long movieId);

  List<FavoriteEntity> findByUserIdOrderByAddedAtDesc (Long userId);
}