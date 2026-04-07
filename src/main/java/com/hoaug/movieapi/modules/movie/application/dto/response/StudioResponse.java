package com.hoaug.movieapi.modules.movie.application.dto.response;

public class StudioResponse {
  private Long id;
  private String name;
  private String slug;
  private String description;
  private String logoUrl;
  private String country;
  private String websiteUrl;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

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
