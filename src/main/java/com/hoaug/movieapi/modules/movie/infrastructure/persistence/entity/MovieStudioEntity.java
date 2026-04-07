package com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import com.hoaug.movieapi.modules.movie.domain.model.MovieStudioRole;

@Entity
@Table(name = "movie_studios", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "movie_id", "studio_id", "role" }) })
public class MovieStudioEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "movie_id", nullable = false)
  private Long movieId;

  @Column(name = "studio_id", nullable = false)
  private Long studioId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private MovieStudioRole role;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

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

  public LocalDateTime getCreatedAt () {
    return createdAt;
  }

  public void setCreatedAt (LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
