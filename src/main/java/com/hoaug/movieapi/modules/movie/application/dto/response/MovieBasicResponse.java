package com.hoaug.movieapi.modules.movie.application.dto.response;

public class MovieBasicResponse {
  private Long id;
  private String title;
  private String slug;
  private String posterUrl;
  private Integer releaseYear;
  private Double averageRating;
  private Long viewCount;
  private Long favoriteCount;

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

  public Double getAverageRating () {
    return averageRating;
  }

  public void setAverageRating (Double averageRating) {
    this.averageRating = averageRating;
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
}
