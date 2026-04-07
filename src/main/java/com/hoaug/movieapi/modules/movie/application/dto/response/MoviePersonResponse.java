package com.hoaug.movieapi.modules.movie.application.dto.response;

public class MoviePersonResponse {
  private Long id;
  private PersonResponse person;
  private String role;
  private String characterName;
  private Integer displayOrder;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

  public PersonResponse getPerson () {
    return person;
  }

  public void setPerson (PersonResponse person) {
    this.person = person;
  }

  public String getRole () {
    return role;
  }

  public void setRole (String role) {
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
