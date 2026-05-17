package com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity;

import com.hoaug.movieapi.common.model.BaseEntity;
import com.hoaug.movieapi.modules.movie.domain.model.MoviePersonRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "movie_persons")
public class MoviePersonEntity extends BaseEntity {

  @Column(name = "movie_id", nullable = false)
  private Long movieId;

  @Column(name = "person_id", nullable = false)
  private Long personId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private MoviePersonRole role;

  @Column(name = "character_name", length = 150)
  private String characterName;

  @Column(name = "display_order", nullable = false)
  private Integer displayOrder;

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
}
