package com.hoaug.movieapi.modules.movie.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovieSummaryResponse {
  private Long id;
  private String title;
  private String originalTitle;
  private String slug;
  private String description;
  private String posterUrl;
  private String bannerUrl;
  private String trailerUrl;
  private Integer releaseYear;
  private String country;
  private String language;
  private String ageRating;
  private String movieType;
  private String movieStatus;
  private Boolean isPremiumOnly;
  private Long viewCount;
  private Long favoriteCount;
  private BigDecimal averageRating;
  private Integer totalRatings;
  private Integer totalReviews;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime publishedAt;

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

  public String getOriginalTitle () {
    return originalTitle;
  }

  public void setOriginalTitle (String originalTitle) {
    this.originalTitle = originalTitle;
  }

  public String getDescription () {
    return description;
  }

  public void setDescription (String description) {
    this.description = description;
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

  public String getMovieStatus () {
    return movieStatus;
  }

  public void setMovieStatus (String movieStatus) {
    this.movieStatus = movieStatus;
  }

  public Long getViewCount () {
    return viewCount;
  }

  public void setViewCount (Long viewCount) {
    this.viewCount = viewCount;
  }

  public Long getFavoriteCount () {
    return favoriteCount;
  }

  public void setFavoriteCount (Long favoriteCount) {
    this.favoriteCount = favoriteCount;
  }

  public Integer getTotalRatings () {
    return totalRatings;
  }

  public void setTotalRatings (Integer totalRatings) {
    this.totalRatings = totalRatings;
  }

  public Integer getTotalReviews () {
    return totalReviews;
  }

  public void setTotalReviews (Integer totalReviews) {
    this.totalReviews = totalReviews;
  }

  public LocalDateTime getCreatedAt () {
    return createdAt;
  }

  public void setCreatedAt (LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt () {
    return updatedAt;
  }

  public void setUpdatedAt (LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public LocalDateTime getPublishedAt () {
    return publishedAt;
  }

  public void setPublishedAt (LocalDateTime publishedAt) {
    this.publishedAt = publishedAt;
  }
}