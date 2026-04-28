package com.hoaug.movieapi.modules.movie.application.usecase;

import java.math.BigDecimal;
import java.util.List;

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

  public AdvancedSearchMovieUseCase(JpaMovieRepository jpaMovieRepository) {
    this.jpaMovieRepository = jpaMovieRepository;
  }

  @Cacheable(cacheNames = "searchResults", key = "#request.keyword + ':' + #request.page + ':' + #request.size + ':' + #request.fromYear + ':' + #request.toYear + ':' + #request.minRating + ':' + #request.sortBy + ':' + #request.sortDirection")
  public SearchMovieResponse execute (SearchMovieRequest request) {
    Sort.Direction direction = "DESC".equalsIgnoreCase(request.getSortDirection())
        ? Sort.Direction.DESC
        : Sort.Direction.ASC;
    Sort sort = Sort.by(direction, request.getSortBy() != null ? request.getSortBy() : "createdAt");
    int pageNumber = request.getPage() != null ? request.getPage() : 0;
    int pageSize = request.getSize() != null ? request.getSize() : 20;
    Pageable candidatePageable = PageRequest.of(0, 10000, sort);

    Page<MovieEntity> page;
    boolean hasKeyword = request.getKeyword() != null && !request.getKeyword().isBlank();

    if (request.getCategoryId() != null && hasKeyword) {
      page = jpaMovieRepository.findByMovieStatusAndCategoryIdAndTitleContaining(
          MovieStatus.PUBLISHED, request.getCategoryId(), request.getKeyword(), candidatePageable);
    } else if (request.getCategoryId() != null) {
      page = jpaMovieRepository.findByMovieStatusAndCategoryId(MovieStatus.PUBLISHED,
          request.getCategoryId(), candidatePageable);
    } else if (hasKeyword) {
      page = jpaMovieRepository.findByMovieStatusAndTitleContaining(MovieStatus.PUBLISHED,
          request.getKeyword(), candidatePageable);
    } else {
      page = jpaMovieRepository.findByMovieStatus(MovieStatus.PUBLISHED, candidatePageable);
    }

    List<MovieEntity> filtered = page.getContent().stream().filter(movie -> {
      if (request.getMovieType() != null && !request.getMovieType().isBlank()
          && !movie.getMovieType().name().equalsIgnoreCase(request.getMovieType())) {
        return false;
      }
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
    }).toList();

    int fromIndex = Math.min(pageNumber * pageSize, filtered.size());
    int toIndex = Math.min(fromIndex + pageSize, filtered.size());
    var content = filtered.subList(fromIndex, toIndex).stream().map(this::toBasicResponse).toList();
    int totalPages = (int) Math.ceil((double) filtered.size() / pageSize);

    return new SearchMovieResponse(content, totalPages, filtered.size(), pageNumber, pageSize);

  }

  private MovieBasicResponse toBasicResponse (MovieEntity movie) {
    MovieBasicResponse res = new MovieBasicResponse();
    res.setId(movie.getId());
    res.setTitle(movie.getTitle());
    res.setSlug(movie.getSlug());
    res.setPosterUrl(movie.getPosterUrl());
    res.setReleaseYear(movie.getReleaseYear());
    res.setAverageRating(movie.getAverageRating().doubleValue());
    res.setViewCount(movie.getViewCount());
    res.setFavoriteCount(movie.getFavoriteCount());
    res.setBannerUrl(movie.getBannerUrl());
    res.setDescription(movie.getDescription());
    res.setMovieType(movie.getMovieType().name());
    return res;
  }
}
