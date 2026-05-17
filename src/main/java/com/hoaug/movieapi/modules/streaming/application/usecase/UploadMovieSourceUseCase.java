package com.hoaug.movieapi.modules.streaming.application.usecase;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.MovieEntity;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaMovieRepository;
import com.hoaug.movieapi.modules.streaming.application.dto.response.MediaUploadResponse;
import com.hoaug.movieapi.modules.streaming.application.service.Mp4StorageService;
import com.hoaug.movieapi.modules.streaming.application.service.StreamUrlService;

@Component
public class UploadMovieSourceUseCase {

  private final JpaMovieRepository movieRepository;
  private final Mp4StorageService storageService;
  private final StreamUrlService streamUrlService;

  public UploadMovieSourceUseCase (JpaMovieRepository movieRepository,
      Mp4StorageService storageService, StreamUrlService streamUrlService) {
    this.movieRepository = movieRepository;
    this.storageService = storageService;
    this.streamUrlService = streamUrlService;
  }

  public MediaUploadResponse execute (Long movieId, MultipartFile file) {
    MovieEntity movie = movieRepository.findById(movieId)
        .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

    storageService.storeMovieSource(movieId, file);

    String videoUrl = streamUrlService.movieMp4Url(movieId);
    movie.setTrailerUrl(videoUrl);
    movieRepository.save(movie);

    return new MediaUploadResponse(movieId, videoUrl, "UPLOADED");
  }
}
