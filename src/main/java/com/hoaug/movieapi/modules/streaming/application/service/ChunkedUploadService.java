package com.hoaug.movieapi.modules.streaming.application.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.streaming.application.config.MediaStorageProperties;

/**
 * Quản lý upload theo chunk để bypass giới hạn body size 100MB của Cloudflare free tunnel.
 * Client chia file thành chunk ≤ 8MB và gửi tuần tự, server ghép lại khi đủ số chunk.
 */
@Component
public class ChunkedUploadService {

  private static final Logger log = LoggerFactory.getLogger(ChunkedUploadService.class);
  private static final long SESSION_TTL_MILLIS = 6L * 60 * 60 * 1000; // 6 giờ

  private final MediaStorageProperties properties;
  private final Path sessionsRoot;
  private final Map<String, Session> sessions = new ConcurrentHashMap<>();

  public ChunkedUploadService(MediaStorageProperties properties,
      @Value("${media.upload.tmp-dir:./data/uploads-tmp}") String tmpDir) {
    this.properties = properties;
    this.sessionsRoot = Path.of(tmpDir).toAbsolutePath().normalize();
    try {
      Files.createDirectories(this.sessionsRoot);
    } catch (IOException ex) {
      throw new IllegalStateException("Không tạo được tmp dir: " + sessionsRoot, ex);
    }
  }

  public Map<String, Object> init(String fileName, long fileSize, int totalChunks) {
    if (fileSize <= 0 || totalChunks <= 0 || totalChunks > 5000) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }
    if (fileSize > properties.getMaxUploadBytes()) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }
    purgeExpired();

    String uploadId = UUID.randomUUID().toString().replace("-", "");
    Path dir = sessionsRoot.resolve(uploadId).normalize();
    if (!dir.startsWith(sessionsRoot)) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }
    try {
      Files.createDirectories(dir);
    } catch (IOException ex) {
      throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
    }

    Session s = new Session();
    s.directory = dir;
    s.fileName = sanitizeFileName(fileName);
    s.totalChunks = totalChunks;
    s.createdAt = Instant.now().toEpochMilli();
    sessions.put(uploadId, s);

    Map<String, Object> resp = new HashMap<>();
    resp.put("uploadId", uploadId);
    resp.put("totalChunks", totalChunks);
    resp.put("expiresInSeconds", SESSION_TTL_MILLIS / 1000);
    return resp;
  }

  public void appendChunk(String uploadId, int chunkIndex, MultipartFile chunk) {
    Session s = requireSession(uploadId);
    if (chunkIndex < 0 || chunkIndex >= s.totalChunks) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }
    if (chunk == null || chunk.isEmpty()) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }
    Path target = s.directory.resolve("chunk-" + chunkIndex);
    try {
      Files.copy(chunk.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
      s.received.put(chunkIndex, true);
    } catch (IOException ex) {
      throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
    }
  }

  /**
   * Ghép tất cả chunk thành 1 file ở đường dẫn destination, xóa session.
   * Caller chịu trách nhiệm validate destination path trước.
   */
  public Path assemble(String uploadId, Path destination) {
    Session s = requireSession(uploadId);
    if (s.received.size() != s.totalChunks) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }

    try {
      Files.createDirectories(destination.getParent());
      Files.deleteIfExists(destination);
      try (var out = Files.newOutputStream(destination, StandardOpenOption.CREATE_NEW,
          StandardOpenOption.WRITE)) {
        byte[] buffer = new byte[1024 * 1024];
        for (int i = 0; i < s.totalChunks; i++) {
          Path chunk = s.directory.resolve("chunk-" + i);
          if (!Files.isRegularFile(chunk)) {
            throw new AppException(ErrorCode.BAD_REQUEST);
          }
          try (InputStream in = Files.newInputStream(chunk)) {
            int n;
            while ((n = in.read(buffer)) > 0) {
              out.write(buffer, 0, n);
            }
          }
        }
      }
      cleanup(s);
      sessions.remove(uploadId);
      return destination;
    } catch (IOException ex) {
      log.error("[ChunkedUpload] assemble failed", ex);
      throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
    }
  }

  public String getFileName(String uploadId) {
    return requireSession(uploadId).fileName;
  }

  public void abort(String uploadId) {
    Session s = sessions.remove(uploadId);
    if (s != null) cleanup(s);
  }

  private Session requireSession(String uploadId) {
    Session s = sessions.get(uploadId);
    if (s == null) {
      throw new AppException(ErrorCode.BAD_REQUEST);
    }
    return s;
  }

  private void purgeExpired() {
    long now = Instant.now().toEpochMilli();
    sessions.entrySet().removeIf(e -> {
      if (now - e.getValue().createdAt > SESSION_TTL_MILLIS) {
        cleanup(e.getValue());
        return true;
      }
      return false;
    });
  }

  private void cleanup(Session s) {
    try {
      if (Files.isDirectory(s.directory)) {
        try (var stream = Files.walk(s.directory)) {
          stream.sorted((a, b) -> b.compareTo(a)).forEach(p -> {
            try { Files.deleteIfExists(p); } catch (IOException ignored) {}
          });
        }
      }
    } catch (IOException ignored) {}
  }

  private String sanitizeFileName(String original) {
    if (original == null || original.isBlank()) return "source.mp4";
    String cleaned = original.replaceAll("[\\\\/]", "_");
    int dot = cleaned.lastIndexOf('.');
    if (dot < 0) return "source.mp4";
    return cleaned.toLowerCase(Locale.ROOT);
  }

  private static class Session {
    Path directory;
    String fileName;
    int totalChunks;
    long createdAt;
    final Map<Integer, Boolean> received = new ConcurrentHashMap<>();
  }
}
