package com.hoaug.movieapi.modules.streaming.application.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.streaming.application.config.MediaStorageProperties;
import com.hoaug.movieapi.modules.streaming.application.dto.request.HlsTranscodeRequest;
import com.hoaug.movieapi.modules.streaming.application.dto.response.HlsTranscodeResult;

import jakarta.annotation.PreDestroy;

@Component
public class HlsTranscodeService {

  private static final Logger log = LoggerFactory.getLogger(HlsTranscodeService.class);
  private final CopyOnWriteArrayList<Process> activeProcesses = new CopyOnWriteArrayList<>();

  @PreDestroy
  public void onShutdown () {
    log.info("[FFmpeg] Shutting down — killing {} active process(es)", activeProcesses.size());
    for (Process p : activeProcesses) {
      if (p.isAlive()) {
        p.destroyForcibly();
        log.info("[FFmpeg] Killed process pid={}", p.pid());
      }
    }
    activeProcesses.clear();
  }

  private static final Map<String, int[]> QUALITY_SETTINGS = Map.of("720p",
      new int[] { 720, 2800, 2996, 4200 }, "1080p", new int[] { 1080, 5000, 5350, 7500 }, "4K",
      new int[] { 2160, 15000, 16050, 22500 });

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
      runFfmpeg(ffmpegPath, request.sourcePath(), playlistPath, segmentPattern, keyInfoPath, null);
      verifyPlaylist(playlistPath);
      return new HlsTranscodeResult(playlistPath, playlistUrl);
    } catch (IOException exception) {
      throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
    } finally {
      deleteQuietly(keyInfoPath);
    }
  }

  public HlsTranscodeResult transcodeQuality (HlsTranscodeRequest request, String quality,
      String playlistUrl) {
    validateSource(request.sourcePath());
    if (!QUALITY_SETTINGS.containsKey(quality)) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }

    int targetHeight = QUALITY_SETTINGS.get(quality)[0];
    int sourceHeight = probeVideoHeight(request.sourcePath());
    if (sourceHeight > 0 && sourceHeight < targetHeight) {
      log.info("[Transcode] Skipping {} — source height {}px < target {}px", quality, sourceHeight,
          targetHeight);
      throw new AppException(ErrorCode.BAD_REQUEST);
    }

    Path ffmpegPath = resolveFfmpegPath();
    Path outputDirectory = request.outputDirectory().toAbsolutePath().normalize();
    Path playlistPath = outputDirectory.resolve("master.m3u8");
    Path segmentPattern = outputDirectory.resolve("segment_%03d.ts");
    Path keyInfoPath = outputDirectory.resolve("key_info.txt");

    try {
      Files.createDirectories(outputDirectory);
      String ivHex = encryptionKeyService.writeNewKey(request.keyPath());
      writeKeyInfoFile(keyInfoPath, request.keyUri(), request.keyPath(), ivHex);
      runFfmpeg(ffmpegPath, request.sourcePath(), playlistPath, segmentPattern, keyInfoPath,
          quality);
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
      Path keyInfoPath, String quality) {
    List<String> cmd = new ArrayList<>(List.of(ffmpegPath.toString(), "-y", "-i",
        sourcePath.toAbsolutePath().normalize().toString(),
        // "-c:v", "libx264",
        // "-c:v", "h264_nvenc", "-c:a", "aac",
        // "-preset", "veryfast"
        // "-preset", "p5", "-tune", "hq"));
        "-c:v", "h264_nvenc", "-preset", "p5", "-tune", "hq", "-rc", "vbr", "-c:a", "aac"));

    if (quality != null && QUALITY_SETTINGS.containsKey(quality)) {
      int[] s = QUALITY_SETTINGS.get(quality);
      cmd.addAll(List.of("-vf", "scale=-2:" + s[0], "-b:v", s[1] + "k", "-maxrate", s[2] + "k",
          "-bufsize", s[3] + "k"));
    }

    cmd.addAll(List.of("-hls_time", "6", "-hls_playlist_type", "vod", "-hls_key_info_file",
        keyInfoPath.toString(), "-hls_segment_filename", segmentPattern.toString(),
        playlistPath.toString()));

    log.debug("[FFmpeg] cmd: {}", String.join(" ", cmd));
    ProcessBuilder processBuilder = new ProcessBuilder(cmd);
    processBuilder.redirectErrorStream(true);

    try {
      Process process = processBuilder.start();
      activeProcesses.add(process);
      try {
        byte[] output = process.getInputStream().readAllBytes();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
          log.error("[FFmpeg] exit={} output=\n{}", exitCode,
              new String(output, StandardCharsets.UTF_8));
          throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
      } finally {
        activeProcesses.remove(process);
      }
    } catch (IOException exception) {
      throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
    } catch (InterruptedException exception) {
      Thread.currentThread().interrupt();
      throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
    }
  }

  /**
   * Uses ffprobe to get the video height. Returns -1 if ffprobe is unavailable or fails.
   */
  private int probeVideoHeight (Path sourcePath) {
    try {
      Path ffmpegBin = Path.of(properties.getFfmpegPath()).toAbsolutePath().normalize().getParent();
      Path ffprobePath = ffmpegBin != null ? ffmpegBin.resolve("ffprobe.exe") : Path.of("ffprobe");
      if (!Files.isRegularFile(ffprobePath)) {
        ffprobePath = Path.of("ffprobe"); // fallback to PATH
      }
      List<String> cmd = List.of(ffprobePath.toString(), "-v", "error", "-select_streams", "v:0",
          "-show_entries", "stream=height", "-of", "csv=p=0",
          sourcePath.toAbsolutePath().normalize().toString());
      Process process = new ProcessBuilder(cmd).redirectErrorStream(true).start();
      byte[] out = process.getInputStream().readAllBytes();
      process.waitFor();
      String heightStr = new String(out, StandardCharsets.UTF_8).trim();
      return heightStr.isEmpty() ? -1 : Integer.parseInt(heightStr);
    } catch (Exception e) {
      log.warn("[Transcode] ffprobe failed, skipping resolution check: {}", e.getMessage());
      return -1;
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
