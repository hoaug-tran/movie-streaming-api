package com.hoaug.movieapi.modules.movie.application.dto.request;

import com.hoaug.movieapi.modules.movie.domain.model.MovieStatus;
import com.hoaug.movieapi.modules.movie.domain.model.MovieType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateMovieRequest {

  @NotBlank
  private String title;

  private String originalTitle;

  @NotBlank
  private String slug;

  private String description;
  private String posterUrl;
  private String bannerUrl;
  private String trailerUrl;

  @NotNull
  private Integer releaseYear;

  private String country;
  private String language;
  private String ageRating;

  @NotNull
  private MovieType movieType;

  @NotNull
  private MovieStatus movieStatus;

  @NotNull
  private Boolean isPremiumOnly;

  public String getTitle () {
    return title;
  }

  public void setTitle (String title) {
    this.title = title;
  }

  public String getOriginalTitle () {
    return originalTitle;
  }

  public void setOriginalTitle (String originalTitle) {
    this.originalTitle = originalTitle;
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

  public String getPosterUrl () {
    return posterUrl;
  }

  public void setPosterUrl (String posterUrl) {
    this.posterUrl = posterUrl;
  }

  public String getBannerUrl () {
    return bannerUrl;
  }

  public void setBannerUrl (String bannerUrl) {
    this.bannerUrl = bannerUrl;
  }

  public String getTrailerUrl () {
    return trailerUrl;
  }

  public void setTrailerUrl (String trailerUrl) {
    this.trailerUrl = trailerUrl;
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

  public MovieType getMovieType () {
    return movieType;
  }

  public void setMovieType (MovieType movieType) {
    this.movieType = movieType;
  }

  public MovieStatus getMovieStatus () {
    return movieStatus;
  }

  public void setMovieStatus (MovieStatus movieStatus) {
    this.movieStatus = movieStatus;
  }

  public Boolean getIsPremiumOnly () {
    return isPremiumOnly;
  }

  public void setIsPremiumOnly (Boolean premiumOnly) {
    isPremiumOnly = premiumOnly;
  }
}
