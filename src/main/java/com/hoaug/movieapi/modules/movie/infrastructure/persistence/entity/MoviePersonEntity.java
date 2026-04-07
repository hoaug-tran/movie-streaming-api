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

import com.hoaug.movieapi.modules.movie.domain.model.MoviePersonRole;

@Entity
@Table(name = "movie_persons", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "movie_id", "person_id", "role", "character_name" }) })
public class MoviePersonEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "movie_id", nullable = false)
  private Long movieId;

  @Column(name = "person_id", nullable = false)
  private Long personId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private MoviePersonRole role;

  @Column(name = "character_name", length = 150)
  private String characterName;

  @Column(name = "display_order", nullable = false)
  private Integer displayOrder;

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

  public Long getPersonId () {
    return personId;
  }

  public void setPersonId (Long personId) {
    this.personId = personId;
  }

  public MoviePersonRole getRole () {
    return role;
  }

  public void setRole (MoviePersonRole role) {
    this.role = role;
  }

  public String getCharacterName () {
    return characterName;
  }

  public void setCharacterName (String characterName) {
    this.characterName = characterName;
  }

  public Integer getDisplayOrder () {
    return displayOrder;
  }

  public void setDisplayOrder (Integer displayOrder) {
    this.displayOrder = displayOrder;
  }

  public LocalDateTime getCreatedAt () {
    return createdAt;
  }

  public void setCreatedAt (LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
