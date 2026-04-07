package com.hoaug.movieapi.modules.favorite.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.favorite.domain.model.Favorite;
import com.hoaug.movieapi.modules.favorite.domain.repository.FavoriteRepository;
import com.hoaug.movieapi.modules.favorite.infrastructure.persistence.entity.FavoriteEntity;
import com.hoaug.movieapi.modules.favorite.infrastructure.persistence.repository.JpaFavoriteRepository;

@Component
public class FavoriteRepositoryAdapter implements FavoriteRepository {

  private final JpaFavoriteRepository jpaFavoriteRepository;

  public FavoriteRepositoryAdapter(JpaFavoriteRepository jpaFavoriteRepository) {
    this.jpaFavoriteRepository = jpaFavoriteRepository;
  }

  @Override
  public Optional<Favorite> findByUserIdAndMovieId (Long userId, Long movieId) {
    return jpaFavoriteRepository.findByUserIdAndMovieId(userId, movieId).map(this::toDomain);
  }

  @Override
  public List<Favorite> findByUserIdOrderByAddedAtDesc (Long userId) {
    return jpaFavoriteRepository.findByUserIdOrderByAddedAtDesc(userId).stream().map(this::toDomain)
        .toList();
  }

  @Override
  public Favorite save (Favorite favorite) {
    FavoriteEntity savedEntity = jpaFavoriteRepository.save(toEntity(favorite));
    return toDomain(savedEntity);
  }

  @Override
  public void delete (Favorite favorite) {
    jpaFavoriteRepository.delete(toEntity(favorite));
  }

  private Favorite toDomain (FavoriteEntity entity) {
    Favorite favorite = new Favorite();
    favorite.setId(entity.getId());
    favorite.setUserId(entity.getUserId());
    favorite.setMovieId(entity.getMovieId());
    favorite.setAddedAt(entity.getAddedAt());
    return favorite;
  }

  private FavoriteEntity toEntity (Favorite favorite) {
    FavoriteEntity entity = new FavoriteEntity();
    entity.setId(favorite.getId());
    entity.setUserId(favorite.getUserId());
    entity.setMovieId(favorite.getMovieId());
    entity.setAddedAt(favorite.getAddedAt());
    return entity;
  }
}