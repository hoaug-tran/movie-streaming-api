package com.hoaug.movieapi.modules.streaming.application.usecase;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.advertisement.infrastructure.persistence.entity.AdvertisementEntity;
import com.hoaug.movieapi.modules.advertisement.infrastructure.persistence.repository.JpaAdvertisementRepository;
import com.hoaug.movieapi.modules.streaming.application.dto.request.HlsTranscodeRequest;
import com.hoaug.movieapi.modules.streaming.application.dto.response.MediaUploadResponse;
import com.hoaug.movieapi.modules.streaming.application.service.HlsPathService;
import com.hoaug.movieapi.modules.streaming.application.service.HlsTranscodeService;
import com.hoaug.movieapi.modules.streaming.application.service.Mp4StorageService;
import com.hoaug.movieapi.modules.streaming.application.service.StreamUrlService;

@Component
public class UploadAdvertisementSourceUseCase {
  private final JpaAdvertisementRepository advertisementRepository;
  private final Mp4StorageService storageService;
  private final StreamUrlService streamUrlService;
  private final HlsPathService hlsPathService;
  private final HlsTranscodeService hlsTranscodeService;

  public UploadAdvertisementSourceUseCase(JpaAdvertisementRepository advertisementRepository,
      Mp4StorageService storageService, StreamUrlService streamUrlService,
      HlsPathService hlsPathService, HlsTranscodeService hlsTranscodeService) {
    this.advertisementRepository = advertisementRepository;
    this.storageService = storageService;
    this.streamUrlService = streamUrlService;
    this.hlsPathService = hlsPathService;
    this.hlsTranscodeService = hlsTranscodeService;
  }

  public MediaUploadResponse execute (Long advertisementId, MultipartFile file) {
    AdvertisementEntity advertisement = advertisementRepository.findById(advertisementId)
        .orElseThrow(() -> new AppException(ErrorCode.ADVERTISEMENT_NOT_FOUND));

    var sourcePath = storageService.storeAdvertisementSource(advertisementId, file);
    String playlistUrl = streamUrlService.advertisementPlaylistUrl(advertisementId);
    hlsTranscodeService.transcode(new HlsTranscodeRequest(
        sourcePath,
        hlsPathService.advertisementOutputDirectory(advertisementId),
        hlsPathService.advertisementKeyPath(advertisementId),
        streamUrlService.advertisementKeyUrl(advertisementId)
    ), playlistUrl);
    advertisement.setVideoUrl(playlistUrl);
    advertisementRepository.save(advertisement);

    return new MediaUploadResponse(advertisementId, playlistUrl, "TRANSCODED");
  }
}
