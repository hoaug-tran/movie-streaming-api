package com.hoaug.movieapi.modules.movie.application.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PersonResponse {
  private Long id;
  private String fullName;
  private String stageName;
  private String biography;
  private LocalDate birthDate;
  private String nationality;
  private String avatarUrl;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

  public String getFullName () {
    return fullName;
  }

  public void setFullName (String fullName) {
    this.fullName = fullName;
  }

  public String getStageName () {
    return stageName;
  }

  public void setStageName (String stageName) {
    this.stageName = stageName;
  }

  public String getBiography () {
    return biography;
  }

  public void setBiography (String biography) {
    this.biography = biography;
  }

  public LocalDate getBirthDate () {
    return birthDate;
  }

  public void setBirthDate (LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public String getNationality () {
    return nationality;
  }

  public void setNationality (String nationality) {
    this.nationality = nationality;
  }

  public String getAvatarUrl () {
    return avatarUrl;
  }

  public void setAvatarUrl (String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }
}
