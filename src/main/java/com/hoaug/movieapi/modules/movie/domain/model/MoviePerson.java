package com.hoaug.movieapi.modules.movie.domain.model;

import java.time.LocalDateTime;

public class MoviePerson {
  private Long id;
  private Long movieId;
  private Long personId;
  private MoviePersonRole role;
  private String characterName;
  private Integer displayOrder;
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
