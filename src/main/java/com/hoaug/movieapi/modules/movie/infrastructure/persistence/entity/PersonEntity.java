package com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity;

import java.time.LocalDate;

import com.hoaug.movieapi.common.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "persons")
public class PersonEntity extends BaseEntity {

  @Column(nullable = false, length = 150)
  private String fullName;

  @Column(name = "stage_name", length = 150)
  private String stageName;

  @Column(columnDefinition = "TEXT")
  private String biography;

  @Column(name = "birth_date")
  private LocalDate birthDate;

  @Column(length = 100)
  private String nationality;

  @Column(name = "avatar_url", length = 500)
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
