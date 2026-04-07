package com.hoaug.movieapi.modules.movie.application.dto.request;

public class CreatePersonRequest {
  private String fullName;
  private String stageName;
  private String biography;
  private String birthDate;
  private String nationality;
  private String avatarUrl;

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

  public String getBirthDate () {
    return birthDate;
  }

  public void setBirthDate (String birthDate) {
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
