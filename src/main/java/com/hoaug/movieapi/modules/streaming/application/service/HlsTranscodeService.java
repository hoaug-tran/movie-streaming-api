package com.hoaug.movieapi.modules.streaming.application.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.streaming.application.config.MediaStorageProperties;
import com.hoaug.movieapi.modules.streaming.application.dto.request.HlsTranscodeRequest;
import com.hoaug.movieapi.modules.streaming.application.dto.response.HlsTranscodeResult;

@Component
public class HlsTranscodeService {
  private final MediaStorageProperties properties;
  private final HlsEncryptionKeyService encryptionKeyService;

  public HlsTranscodeService(MediaStorageProperties properties,
      HlsEncryptionKeyService encryptionKeyService) {
    this.properties = properties;
    this.encryptionKeyService = encryptionKeyService;
  }

  public HlsTranscodeResult transcode (HlsTranscodeRequest request, String playlistUrl) {
    validateSource(request.sourcePath());
    Path ffmpegPath = resolveFfmpegPath();
    Path outputDirectory = request.outputDirectory().toAbsolutePath().normalize();
    Path playlistPath = outputDirectory.resolve("master.m3u8");
    Path segmentPattern = outputDirectory.resolve("segment_%03d.ts");
    Path keyInfoPath = outputDirectory.resolve("key_info.txt");

    try {
      Files.createDirectories(outputDirectory);
      String ivHex = encryptionKeyService.writeNewKey(request.keyPath());
      writeKeyInfoFile(keyInfoPath, request.keyUri(), request.keyPath(), ivHex);
      runFfmpeg(ffmpegPath, request.sourcePath(), playlistPath, segmentPattern, keyInfoPath);
      verifyPlaylist(playlistPath);
      return new HlsTranscodeResult(playlistPath, playlistUrl);
    } catch (IOException exception) {
      throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
    } finally {
      deleteQuietly(keyInfoPath);
    }
  }

  private Path resolveFfmpegPath () {
    Path path = Path.of(properties.getFfmpegPath()).toAbsolutePath().normalize();
    if (!Files.isRegularFile(path)) {
      throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
    }
    return path;
  }

  private void validateSource (Path sourcePath) {
    if (sourcePath == null || !Files.isRegularFile(sourcePath)) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }
  }

  private void writeKeyInfoFile (Path keyInfoPath, String keyUri, Path keyPath, String ivHex) {
    List<String> lines = List.of(keyUri, keyPath.toAbsolutePath().normalize().toString(), ivHex);
    try {
      Files.write(keyInfoPath, lines);
    } catch (IOException exception) {
      throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
    }
  }

  private void runFfmpeg (Path ffmpegPath, Path sourcePath, Path playlistPath, Path segmentPattern,
      Path keyInfoPath) {
    ProcessBuilder processBuilder = new ProcessBuilder(
        ffmpegPath.toString(),
        "-y",
        "-i", sourcePath.toAbsolutePath().normalize().toString(),
        "-c:v", "libx264",
        "-c:a", "aac",
        "-preset", "veryfast",
        "-hls_time", "6",
        "-hls_playlist_type", "vod",
        "-hls_key_info_file", keyInfoPath.toString(),
        "-hls_segment_filename", segmentPattern.toString(),
        playlistPath.toString()
    );
    processBuilder.redirectErrorStream(true);

    try {
      Process process = processBuilder.start();
      String output = new String(process.getInputStream().readAllBytes());
      int exitCode = process.waitFor();
      if (exitCode != 0) {
        throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
      }
    } catch (IOException exception) {
      throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
    } catch (InterruptedException exception) {
      Thread.currentThread().interrupt();
      throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
    }
  }

  private void verifyPlaylist (Path playlistPath) {
    if (!Files.isRegularFile(playlistPath)) {
      throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
    }
  }

  private void deleteQuietly (Path path) {
    try {
      Files.deleteIfExists(path);
    } catch (IOException ignored) {
    }
  }
}
