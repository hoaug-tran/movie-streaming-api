package com.hoaug.movieapi.modules.movie.application.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieBasicResponse {
  private Long id;
  private String title;
  private String slug;
  private String posterUrl;
  private String bannerUrl;
  private String description;
  private String trailerUrl;
  private String movieType;
  private Integer releaseYear;
  private Double averageRating;
  private Long viewCount;
  private Long favoriteCount;
  private List<CategoryResponse> categories;
}
