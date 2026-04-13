package com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity;

import com.hoaug.movieapi.common.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "studios")
public class StudioEntity extends BaseEntity {

  @Column(nullable = false, length = 150)
  private String name;

  @Column(nullable = false, unique = true, length = 180)
  private String slug;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(name = "logo_url", length = 500)
  private String logoUrl;

  @Column(length = 100)
  private String country;

  @Column(name = "website_url", length = 500)
  private String websiteUrl;

  public String getName () {
    return name;
  }

  public void setName (String name) {
    this.name = name;
  }

  public String getSlug () {
    return slug;
  }

  public void setSlug (String slug) {
    this.slug = slug;
  }

  public String getDescription () {
    return description;
  }

  public void setDescription (String description) {
    this.description = description;
  }

  public String getLogoUrl () {
    return logoUrl;
  }

  public void setLogoUrl (String logoUrl) {
    this.logoUrl = logoUrl;
  }

  public String getCountry () {
    return country;
  }

  public void setCountry (String country) {
    this.country = country;
  }

  public String getWebsiteUrl () {
    return websiteUrl;
  }

  public void setWebsiteUrl (String websiteUrl) {
    this.websiteUrl = websiteUrl;
  }
}
