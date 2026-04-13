package com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity;

import com.hoaug.movieapi.common.model.BaseEntity;
import com.hoaug.movieapi.modules.movie.domain.model.MovieStudioRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "movie_studios", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "movie_id", "studio_id", "role" }) })
public class MovieStudioEntity extends BaseEntity {

  @Column(name = "movie_id", nullable = false)
  private Long movieId;

  @Column(name = "studio_id", nullable = false)
  private Long studioId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private MovieStudioRole role;

  public Long getMovieId () {
    return movieId;
  }

  public void setMovieId (Long movieId) {
    this.movieId = movieId;
  }

  public Long getStudioId () {
    return studioId;
  }

  public void setStudioId (Long studioId) {
    this.studioId = studioId;
  }

  public MovieStudioRole getRole () {
    return role;
  }

  public void setRole (MovieStudioRole role) {
    this.role = role;
  }
}
