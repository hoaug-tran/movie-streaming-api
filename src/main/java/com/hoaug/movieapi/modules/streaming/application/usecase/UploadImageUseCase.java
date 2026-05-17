package com.hoaug.movieapi.modules.streaming.application.usecase;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.hoaug.movieapi.modules.streaming.application.dto.response.MediaUploadResponse;
import com.hoaug.movieapi.modules.streaming.application.service.ImageStorageService;
import com.hoaug.movieapi.modules.streaming.application.service.StreamUrlService;

@Component
public class UploadImageUseCase {

  private final ImageStorageService imageStorageService;
  private final StreamUrlService streamUrlService;

  public UploadImageUseCase (ImageStorageService imageStorageService,
      StreamUrlService streamUrlService) {
    this.imageStorageService = imageStorageService;
    this.streamUrlService = streamUrlService;
  }

  public MediaUploadResponse execute (MultipartFile file) {
    String filename = imageStorageService.store(file);
    String url = streamUrlService.imageUrl(filename);
    return new MediaUploadResponse(null, url, "UPLOADED");
  }
}
