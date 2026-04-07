package com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "movie_tags")
@IdClass(MovieTagEntity.MovieTagId.class)
public class MovieTagEntity {

  @Id
  @Column(name = "movie_id")
  private Long movieId;

  @Id
  @Column(name = "tag_id")
  private Long tagId;

  public Long getMovieId () {
    return movieId;
  }

  public void setMovieId (Long movieId) {
    this.movieId = movieId;
  }

  public Long getTagId () {
    return tagId;
  }

  public void setTagId (Long tagId) {
    this.tagId = tagId;
  }

  public static class MovieTagId implements Serializable {
    private Long movieId;
    private Long tagId;

    public MovieTagId() {
    }

    public MovieTagId(Long movieId, Long tagId) {
      this.movieId = movieId;
      this.tagId = tagId;
    }

    public Long getMovieId () {
      return movieId;
    }

    public void setMovieId (Long movieId) {
      this.movieId = movieId;
    }

    public Long getTagId () {
      return tagId;
    }

    public void setTagId (Long tagId) {
      this.tagId = tagId;
    }

    @Override
    public boolean equals (Object o) {
      if (this == o)
        return true;
      if (o == null || getClass() != o.getClass())
        return false;

      MovieTagId that = (MovieTagId) o;

      if (!movieId.equals(that.movieId))
        return false;
      return tagId.equals(that.tagId);
    }

    @Override
    public int hashCode () {
      int result = movieId.hashCode();
      result = 31 * result + tagId.hashCode();
      return result;
    }
  }
}
