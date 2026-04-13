package com.hoaug.movieapi.modules.movie.application.dto.request;

import com.hoaug.movieapi.common.validator.ValidSafeString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreatePersonRequest {
  @NotBlank(message = "Full name is required")
  @Size(min = 1, max = 255, message = "Full name must be between 1 and 255 characters")
  @ValidSafeString(minLength = 1, maxLength = 255)
  private String fullName;

  @Size(max = 255, message = "Stage name must be at most 255 characters")
  @ValidSafeString(minLength = 0, maxLength = 255)
  private String stageName;

  @Size(max = 2000, message = "Biography must be at most 2000 characters")
  @ValidSafeString(minLength = 0, maxLength = 2000)
  private String biography;

  @Size(max = 20, message = "Birth date must be at most 20 characters")
  @ValidSafeString(minLength = 0, maxLength = 20)
  private String birthDate;

  @Size(max = 100, message = "Nationality must be at most 100 characters")
  @ValidSafeString(minLength = 0, maxLength = 100)
  private String nationality;

  @Size(max = 500, message = "Avatar URL must be at most 500 characters")
  @ValidSafeString(minLength = 0, maxLength = 500)
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
