package com.hoaug.movieapi.modules.movie.application.dto.response;

import java.math.BigDecimal;

public class MovieSummaryResponse {
  private Long id;
  private String title;
  private String slug;
  private String posterUrl;
  private Integer releaseYear;
  private String country;
  private String language;
  private String ageRating;
  private String movieType;
  private Boolean isPremiumOnly;
  private BigDecimal averageRating;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

  public String getTitle () {
    return title;
  }

  public void setTitle (String title) {
    this.title = title;
  }

  public String getSlug () {
    return slug;
  }

  public void setSlug (String slug) {
    this.slug = slug;
  }

  public String getPosterUrl () {
    return posterUrl;
  }

  public void setPosterUrl (String posterUrl) {
    this.posterUrl = posterUrl;
  }

  public Integer getReleaseYear () {
    return releaseYear;
  }

  public void setReleaseYear (Integer releaseYear) {
    this.releaseYear = releaseYear;
  }

  public String getCountry () {
    return country;
  }

  public void setCountry (String country) {
    this.country = country;
  }

  public String getLanguage () {
    return language;
  }

  public void setLanguage (String language) {
    this.language = language;
  }

  public String getAgeRating () {
    return ageRating;
  }

  public void setAgeRating (String ageRating) {
    this.ageRating = ageRating;
  }

  public String getMovieType () {
    return movieType;
  }

  public void setMovieType (String movieType) {
    this.movieType = movieType;
  }

  public Boolean getIsPremiumOnly () {
    return isPremiumOnly;
  }

  public void setIsPremiumOnly (Boolean premiumOnly) {
    isPremiumOnly = premiumOnly;
  }

  public BigDecimal getAverageRating () {
    return averageRating;
  }

  public void setAverageRating (BigDecimal averageRating) {
    this.averageRating = averageRating;
  }
}