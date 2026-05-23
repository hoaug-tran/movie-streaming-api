package com.hoaug.movieapi.modules.streaming.application.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
    log.info("[FFmpeg] Shutting down - killing {} active process(es)", activeProcesses.size());
    for (Process p : activeProcesses) {
      if (p.isAlive()) {
        p.destroyForcibly();
        log.info("[FFmpeg] Killed process pid={}", p.pid());
      }
    }
    activeProcesses.clear();
  }

  private static final Map<String, int[]> QUALITY_SETTINGS = Map.of("720p",
      new int[] { 720, 3500, 4500, 7000 }, "1080p", new int[] { 1080, 6500, 8000, 13000 }, "4K",
      new int[] { 2160, 18000, 22000, 36000 });

  private static final Map<String, String> QUALITY_LEVELS = Map.of("720p", "4.0", "1080p", "4.2",
      "4K", "5.2");

  private static final int SEGMENT_DURATION = 4;
  private static final int DEFAULT_FPS = 30;
  private static final int MAX_OUTPUT_FPS = 30;

  private final MediaStorageProperties properties;
  private final HlsEncryptionKeyService encryptionKeyService;

  public HlsTranscodeService(MediaStorageProperties properties,
      HlsEncryptionKeyService encryptionKeyService) {
    this.properties = properties;
    this.encryptionKeyService = encryptionKeyService;
  }

  public HlsTranscodeResult transcode (HlsTranscodeRequest request, String playlistUrl) {
    return runQuality(request, null, playlistUrl);
  }

  public HlsTranscodeResult transcodeQuality (HlsTranscodeRequest request, String quality,
      String playlistUrl) {
    if (!QUALITY_SETTINGS.containsKey(quality)) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }
    return runQuality(request, quality, playlistUrl);
  }

  private HlsTranscodeResult runQuality (HlsTranscodeRequest request, String quality,
      String playlistUrl) {
    validateSource(request.sourcePath());
    Path ffmpegPath = resolveFfmpegPath();
    Path outputDirectory = request.outputDirectory().toAbsolutePath().normalize();
    Path playlistPath = outputDirectory.resolve("master.m3u8");
    String segmentPrefix = sanitizePrefix(request.segmentPrefix());
    Path segmentPattern = outputDirectory.resolve(segmentPrefix + "segment_%03d.ts");
    Path keyInfoPath = outputDirectory.resolve("key_info.txt");

    SourceProbe probe = probeSource(request.sourcePath());

    if (quality != null) {
      int targetHeight = QUALITY_SETTINGS.get(quality)[0];
      if (probe.height > 0 && probe.height < targetHeight
          && !isAllowedUpscale(probe.height, targetHeight)) {
        log.info("[Transcode] Skipping {} - source height {}px < target {}px", quality,
            probe.height, targetHeight);
        throw new AppException(ErrorCode.BAD_REQUEST);
      }
    }

    try {
      Files.createDirectories(outputDirectory);
      cleanupOldSegments(outputDirectory, segmentPrefix);
      String ivHex = encryptionKeyService.writeNewKey(request.keyPath());
      writeKeyInfoFile(keyInfoPath, request.keyUri(), request.keyPath(), ivHex);
      runFfmpegWithFallback(ffmpegPath, request.sourcePath(), playlistPath, segmentPattern,
          keyInfoPath, quality, probe);
      verifyPlaylist(playlistPath);
      return new HlsTranscodeResult(playlistPath, playlistUrl);
    } catch (IOException exception) {
      throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
    } finally {
      deleteQuietly(keyInfoPath);
    }
  }

  private boolean isAllowedUpscale (int sourceHeight, int targetHeight) {
    return sourceHeight >= 1000 && targetHeight == 2160;
  }

  private String sanitizePrefix (String prefix) {
    if (prefix == null || prefix.isBlank())
      return "";
    String cleaned = prefix.replaceAll("[^A-Za-z0-9_\\-]", "");
    return cleaned.isEmpty() ? "" : cleaned + "_";
  }

  private void cleanupOldSegments (Path outputDirectory, String currentPrefix) {
    if (!Files.isDirectory(outputDirectory))
      return;
    try (var stream = Files.list(outputDirectory)) {
      stream.filter(p -> {
        String name = p.getFileName().toString();
        if (!name.endsWith(".ts"))
          return false;
        return !name.startsWith(currentPrefix);
      }).forEach(p -> {
        try {
          Files.deleteIfExists(p);
        } catch (IOException ignored) {
        }
      });
    } catch (IOException ex) {
      log.warn("[Transcode] cleanup old segments failed in {}: {}", outputDirectory,
          ex.getMessage());
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

  private void runFfmpegWithFallback (Path ffmpegPath, Path sourcePath, Path playlistPath,
      Path segmentPattern, Path keyInfoPath, String quality, SourceProbe probe) {
    try {
      runFfmpeg(ffmpegPath, sourcePath, playlistPath, segmentPattern, keyInfoPath, quality, probe,
          EncoderMode.NVENC);
      return;
    } catch (AppException nvencFailure) {
      log.warn("[FFmpeg] NVENC failed for quality={} - retrying with libx264 software encoder",
          quality);
    }

    runFfmpeg(ffmpegPath, sourcePath, playlistPath, segmentPattern, keyInfoPath, quality, probe,
        EncoderMode.LIBX264);
  }

  private enum EncoderMode {
    NVENC,
    LIBX264
  }

  private void runFfmpeg (Path ffmpegPath, Path sourcePath, Path playlistPath, Path segmentPattern,
      Path keyInfoPath, String quality, SourceProbe probe, EncoderMode mode) {
    int sourceFps = probe.fps > 0 ? probe.fps : DEFAULT_FPS;
    int fps = Math.min(sourceFps, MAX_OUTPUT_FPS);
    int gop = SEGMENT_DURATION * fps;
    boolean useNvenc = mode != EncoderMode.LIBX264;

    List<String> cmd = new ArrayList<>();
    cmd.add(ffmpegPath.toString());
    cmd.add("-y");
    cmd.add("-hide_banner");
    cmd.add("-loglevel");
    cmd.add("error");
    cmd.add("-probesize");
    cmd.add("100M");
    cmd.add("-analyzeduration");
    cmd.add("100M");
    cmd.add("-fflags");
    cmd.add("+genpts");
    cmd.add("-i");
    cmd.add(sourcePath.toAbsolutePath().normalize().toString());
    cmd.add("-map");
    cmd.add("0:v:0");
    cmd.add("-map");
    cmd.add("0:a:0?");
    cmd.add("-c:v");
    cmd.add(useNvenc ? "h264_nvenc" : "libx264");
    cmd.add("-profile:v");
    cmd.add("high");
    cmd.add("-pix_fmt");
    cmd.add("yuv420p");
    if (useNvenc) {
      cmd.add("-preset");
      cmd.add("p5");
      cmd.add("-tune");
      cmd.add("hq");
      cmd.add("-rc");
      cmd.add("vbr");
      cmd.add("-multipass");
      cmd.add("qres");
      cmd.add("-bf");
      cmd.add("2");
      cmd.add("-b_ref_mode");
      cmd.add("disabled");
      cmd.add("-spatial_aq");
      cmd.add("1");
      cmd.add("-aq-strength");
      cmd.add("8");
      cmd.add("-temporal_aq");
      cmd.add("1");
      cmd.add("-rc-lookahead");
      cmd.add("20");
    } else {
      cmd.add("-preset");
      cmd.add("veryfast");
      cmd.add("-tune");
      cmd.add("film");
      cmd.add("-x264-params");
      cmd.add("keyint=" + gop + ":min-keyint=" + gop + ":scenecut=0");
    }
    cmd.add("-r");
    cmd.add(String.valueOf(fps));
    cmd.add("-g");
    cmd.add(String.valueOf(gop));
    cmd.add("-keyint_min");
    cmd.add(String.valueOf(gop));
    cmd.add("-sc_threshold");
    cmd.add("0");
    cmd.add("-fps_mode");
    cmd.add("cfr");

    if (quality != null && QUALITY_SETTINGS.containsKey(quality)) {
      int[] s = QUALITY_SETTINGS.get(quality);
      String level = QUALITY_LEVELS.getOrDefault(quality, "4.2");
      String scaleAlgo = (probe.height > 0 && probe.height < s[0]) ? "lanczos" : "bicubic";
      String filter = String.format(Locale.ROOT, "scale=-2:%d:flags=%s,format=yuv420p", s[0], scaleAlgo);
      cmd.add("-vf");
      cmd.add(filter);
      if (!useNvenc) {
        cmd.add("-level:v");
        cmd.add(level);
      }
      cmd.add("-b:v");
      cmd.add(s[1] + "k");
      cmd.add("-maxrate");
      cmd.add(s[2] + "k");
      cmd.add("-bufsize");
      cmd.add(s[3] + "k");
    } else {
      cmd.add("-level:v");
      cmd.add("4.2");
      cmd.add("-b:v");
      cmd.add("5000k");
      cmd.add("-maxrate");
      cmd.add("6000k");
      cmd.add("-bufsize");
      cmd.add("10000k");
    }

    cmd.add("-c:a");
    cmd.add("aac");
    cmd.add("-b:a");
    cmd.add("192k");
    cmd.add("-ac");
    cmd.add("2");
    cmd.add("-ar");
    cmd.add("48000");

    cmd.add("-hls_time");
    cmd.add(String.valueOf(SEGMENT_DURATION));
    cmd.add("-hls_playlist_type");
    cmd.add("vod");
    cmd.add("-hls_segment_type");
    cmd.add("mpegts");
    cmd.add("-hls_flags");
    cmd.add("independent_segments+temp_file+delete_segments");
    cmd.add("-hls_key_info_file");
    cmd.add(keyInfoPath.toString());
    cmd.add("-hls_segment_filename");
    cmd.add(segmentPattern.toString());
    cmd.add(playlistPath.toString());

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
          log.error("[FFmpeg] exit={} mode={} quality={} sourceFps={} outputFps={} output=\n{}", exitCode,
              mode, quality, sourceFps, fps, new String(output, StandardCharsets.UTF_8));
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

  private static final class SourceProbe {
    final int height;
    final int fps;

    SourceProbe(int height, int fps) {
      this.height = height;
      this.fps = fps;
    }
  }

  private SourceProbe probeSource (Path sourcePath) {
    try {
      Path ffmpegBin = Path.of(properties.getFfmpegPath()).toAbsolutePath().normalize().getParent();
      Path ffprobePath = ffmpegBin != null ? ffmpegBin.resolve("ffprobe.exe") : Path.of("ffprobe");
      if (!Files.isRegularFile(ffprobePath)) {
        ffprobePath = Path.of("ffprobe");
      }
      List<String> cmd = List.of(ffprobePath.toString(), "-v", "error", "-select_streams", "v:0",
          "-show_entries", "stream=height,r_frame_rate", "-of", "csv=p=0",
          sourcePath.toAbsolutePath().normalize().toString());
      Process process = new ProcessBuilder(cmd).redirectErrorStream(true).start();
      byte[] out = process.getInputStream().readAllBytes();
      process.waitFor();
      String raw = new String(out, StandardCharsets.UTF_8).trim();
      if (raw.isEmpty())
        return new SourceProbe(-1, -1);
      String[] parts = raw.split(",");
      int height = parts.length > 0 ? safeParseInt(parts[0].trim(), -1) : -1;
      int fps = parts.length > 1 ? parseFraction(parts[1].trim()) : -1;
      log.debug("[Transcode] probe height={} fps={} for {}", height, fps, sourcePath);
      return new SourceProbe(height, fps);
    } catch (Exception e) {
      log.warn("[Transcode] ffprobe failed: {}", e.getMessage());
      return new SourceProbe(-1, -1);
    }
  }

  private int parseFraction (String raw) {
    if (raw == null || raw.isEmpty())
      return -1;
    int slash = raw.indexOf('/');
    try {
      if (slash < 0)
        return Math.round(Float.parseFloat(raw));
      int num = safeParseInt(raw.substring(0, slash), -1);
      int den = safeParseInt(raw.substring(slash + 1), 1);
      if (num <= 0 || den <= 0)
        return -1;
      return Math.max(1, Math.round((float) num / den));
    } catch (NumberFormatException e) {
      return -1;
    }
  }

  private int safeParseInt (String raw, int fallback) {
    try {
      return Integer.parseInt(raw);
    } catch (NumberFormatException e) {
      return fallback;
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
