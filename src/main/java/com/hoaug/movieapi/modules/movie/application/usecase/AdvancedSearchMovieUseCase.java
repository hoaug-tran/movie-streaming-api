package com.hoaug.movieapi.modules.movie.application.usecase;

import java.math.BigDecimal;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.request.SearchMovieRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieBasicResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.SearchMovieResponse;
import com.hoaug.movieapi.modules.movie.domain.model.MovieStatus;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.MovieEntity;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaMovieRepository;

@Component
public class AdvancedSearchMovieUseCase {
  private final JpaMovieRepository jpaMovieRepository;
  private final GetMovieCategoriesUseCase getMovieCategoriesUseCase;

  public AdvancedSearchMovieUseCase(JpaMovieRepository jpaMovieRepository,
      GetMovieCategoriesUseCase getMovieCategoriesUseCase) {
    this.jpaMovieRepository = jpaMovieRepository;
    this.getMovieCategoriesUseCase = getMovieCategoriesUseCase;
  }

  @Cacheable(cacheNames = "searchResults", key = "#request.keyword + ':' + #request.page + ':' + #request.size + ':' + #request.fromYear + ':' + #request.toYear + ':' + #request.minRating + ':' + #request.sortBy + ':' + #request.sortDirection")
  public SearchMovieResponse execute (SearchMovieRequest request) {
    Sort.Direction direction = "DESC".equalsIgnoreCase(request.getSortDirection())
        ? Sort.Direction.DESC
        : Sort.Direction.ASC;
    Sort sort = Sort.by(direction, request.getSortBy() != null ? request.getSortBy() : "createdAt");
    Pageable pageable = PageRequest.of(request.getPage() != null ? request.getPage() : 0,
        request.getSize() != null ? request.getSize() : 20, sort);

    Page<MovieEntity> page = jpaMovieRepository.findByMovieStatus(MovieStatus.PUBLISHED, pageable);

    if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
      page = jpaMovieRepository.findByMovieStatusAndTitleContaining(MovieStatus.PUBLISHED,
          request.getKeyword(), pageable);
    }

    var content = page.getContent().stream().filter(movie -> {
      if (request.getFromYear() != null && movie.getReleaseYear() < request.getFromYear()) {
        return false;
      }
      if (request.getToYear() != null && movie.getReleaseYear() > request.getToYear()) {
        return false;
      }
      if (request.getMinRating() != null
          && movie.getAverageRating().compareTo(BigDecimal.valueOf(request.getMinRating())) < 0) {
        return false;
      }
      return true;
    }).map(this::toBasicResponse).toList();

    return new SearchMovieResponse(content, page.getTotalPages(), page.getTotalElements(),
        page.getNumber(), page.getSize());
  }

  private MovieBasicResponse toBasicResponse (MovieEntity movie) {
    MovieBasicResponse res = new MovieBasicResponse();
    res.setId(movie.getId());
    res.setTitle(movie.getTitle());
    res.setSlug(movie.getSlug());
    res.setPosterUrl(movie.getPosterUrl());
    res.setBannerUrl(movie.getBannerUrl());
    res.setDescription(movie.getDescription());
    res.setTrailerUrl(movie.getTrailerUrl());
    res.setMovieType(movie.getMovieType() != null ? movie.getMovieType().name() : null);
    res.setReleaseYear(movie.getReleaseYear());
    res.setAverageRating(
        movie.getAverageRating() != null ? movie.getAverageRating().doubleValue() : 0.0);
    res.setViewCount(movie.getViewCount());
    res.setFavoriteCount(movie.getFavoriteCount());
    res.setCategories(getMovieCategoriesUseCase.execute(movie.getId()));
    return res;
  }
}
