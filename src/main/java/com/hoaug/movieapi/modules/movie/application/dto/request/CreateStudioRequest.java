package com.hoaug.movieapi.modules.movie.application.dto.request;

import com.hoaug.movieapi.common.validator.ValidSafeString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateStudioRequest {
  @NotBlank(message = "Studio name is required")
  @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
  @ValidSafeString(minLength = 1, maxLength = 255)
  private String name;

  @NotBlank(message = "Studio slug is required")
  @Size(min = 1, max = 270, message = "Slug must be between 1 and 270 characters")
  @ValidSafeString(minLength = 1, maxLength = 270)
  private String slug;

  @Size(max = 2000, message = "Description must be at most 2000 characters")
  @ValidSafeString(minLength = 0, maxLength = 2000)
  private String description;

  @Size(max = 500, message = "Logo URL must be at most 500 characters")
  @ValidSafeString(minLength = 0, maxLength = 500)
  private String logoUrl;

  @Size(max = 100, message = "Country must be at most 100 characters")
  @ValidSafeString(minLength = 0, maxLength = 100)
  private String country;

  @Size(max = 2048, message = "Website URL must be at most 2048 characters")
  @ValidSafeString(minLength = 0, maxLength = 2048)
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
