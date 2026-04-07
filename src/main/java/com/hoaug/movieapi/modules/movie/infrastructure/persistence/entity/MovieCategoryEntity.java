package com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "movie_categories")
@IdClass(MovieCategoryEntity.MovieCategoryId.class)
public class MovieCategoryEntity {

  @Id
  @Column(name = "movie_id")
  private Long movieId;

  @Id
  @Column(name = "category_id")
  private Long categoryId;

  public Long getMovieId () {
    return movieId;
  }

  public void setMovieId (Long movieId) {
    this.movieId = movieId;
  }

  public Long getCategoryId () {
    return categoryId;
  }

  public void setCategoryId (Long categoryId) {
    this.categoryId = categoryId;
  }

  public static class MovieCategoryId implements Serializable {
    private Long movieId;
    private Long categoryId;

    public MovieCategoryId() {
    }

    public MovieCategoryId(Long movieId, Long categoryId) {
      this.movieId = movieId;
      this.categoryId = categoryId;
    }

    public Long getMovieId () {
      return movieId;
    }

    public void setMovieId (Long movieId) {
      this.movieId = movieId;
    }

    public Long getCategoryId () {
      return categoryId;
    }

    public void setCategoryId (Long categoryId) {
      this.categoryId = categoryId;
    }

    @Override
    public boolean equals (Object o) {
      if (this == o)
        return true;
      if (o == null || getClass() != o.getClass())
        return false;

      MovieCategoryId that = (MovieCategoryId) o;

      if (!movieId.equals(that.movieId))
        return false;
      return categoryId.equals(that.categoryId);
    }

    @Override
    public int hashCode () {
      int result = movieId.hashCode();
      result = 31 * result + categoryId.hashCode();
      return result;
    }
  }
}
