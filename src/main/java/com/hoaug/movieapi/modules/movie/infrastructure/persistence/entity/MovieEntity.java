package com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.hoaug.movieapi.common.model.BaseEntity;
import com.hoaug.movieapi.modules.movie.domain.model.MovieStatus;
import com.hoaug.movieapi.modules.movie.domain.model.MovieType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "movies")
public class MovieEntity extends BaseEntity {

  @Column(nullable = false, length = 255)
  private String title;

  @Column(name = "original_title", length = 255)
  private String originalTitle;

  @Column(nullable = false, unique = true, length = 255)
  private String slug;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(name = "poster_url", length = 500)
  private String posterUrl;

  @Column(name = "banner_url", length = 500)
  private String bannerUrl;

  @Column(name = "trailer_url", length = 500)
  private String trailerUrl;

  @Column(name = "release_year", nullable = false)
  private Integer releaseYear;

  @Column(length = 100)
  private String country;

  @Column(length = 100)
  private String language;

  @Column(name = "age_rating", length = 20)
  private String ageRating;

  @Enumerated(EnumType.STRING)
  @Column(name = "movie_status", nullable = false, length = 20)
  private MovieStatus movieStatus;

  @Enumerated(EnumType.STRING)
  @Column(name = "movie_type", nullable = false, length = 20)
  private MovieType movieType;

  @Column(name = "is_premium_only", nullable = false)
  private Boolean isPremiumOnly;

  @Column(name = "view_count", nullable = false)
  private Long viewCount;

  @Column(name = "favorite_count", nullable = false)
  private Long favoriteCount;

  @Column(name = "average_rating", nullable = false, precision = 3, scale = 2)
  private BigDecimal averageRating;

  @Column(name = "total_ratings", nullable = false)
  private Integer totalRatings;

  @Column(name = "total_reviews", nullable = false)
  private Integer totalReviews;

  @Column(name = "published_at")
  private LocalDateTime publishedAt;

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

  public MovieStatus getMovieStatus () {
    return movieStatus;
  }

  public void setMovieStatus (MovieStatus movieStatus) {
    this.movieStatus = movieStatus;
  }

  public MovieType getMovieType () {
    return movieType;
  }

  public void setMovieType (MovieType movieType) {
    this.movieType = movieType;
  }

  public Boolean getIsPremiumOnly () {
    return isPremiumOnly;
  }

  public void setIsPremiumOnly (Boolean premiumOnly) {
    isPremiumOnly = premiumOnly;
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

  public BigDecimal getAverageRating () {
    return averageRating;
  }

  public void setAverageRating (BigDecimal averageRating) {
    this.averageRating = averageRating;
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

  public LocalDateTime getPublishedAt () {
    return publishedAt;
  }

  public void setPublishedAt (LocalDateTime publishedAt) {
    this.publishedAt = publishedAt;
  }
}