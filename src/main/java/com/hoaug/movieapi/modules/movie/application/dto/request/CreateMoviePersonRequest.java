package com.hoaug.movieapi.modules.movie.application.dto.request;

public class CreateMoviePersonRequest {
  private Long personId;
  private String role;
  private String characterName;
  private Integer displayOrder;

  public Long getPersonId () {
    return personId;
  }

  public void setPersonId (Long personId) {
    this.personId = personId;
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
