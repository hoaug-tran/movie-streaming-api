package com.hoaug.movieapi.modules.system.application;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

import com.hoaug.movieapi.common.config.PayOSConfig;
import com.hoaug.movieapi.modules.streaming.application.config.MediaStorageProperties;
import com.hoaug.movieapi.modules.system.domain.SystemStatusResponse;
import com.hoaug.movieapi.modules.system.domain.SystemStatusResponse.ComponentStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SystemStatusService {

  private static final String STATUS_OPERATIONAL = "operational";
  private static final String STATUS_DEGRADED = "degraded";
  private static final String STATUS_OUTAGE = "outage";
  private static final String STATUS_MAINTENANCE = "maintenance";

  private final DataSource dataSource;
  private final RedisConnectionFactory redisConnectionFactory;
  private final PayOSConfig payOSConfig;
  private final MediaStorageProperties mediaStorageProperties;
  private final String mailUsername;
  private final String mailHost;
  private final String appVersion;

  public SystemStatusService(
      DataSource dataSource,
      RedisConnectionFactory redisConnectionFactory,
      PayOSConfig payOSConfig,
      MediaStorageProperties mediaStorageProperties,
      @Value("${spring.mail.username:}") String mailUsername,
      @Value("${spring.mail.host:}") String mailHost,
      @Value("${spring.application.name:movie-streaming-api}") String appVersion) {
    this.dataSource = dataSource;
    this.redisConnectionFactory = redisConnectionFactory;
    this.payOSConfig = payOSConfig;
    this.mediaStorageProperties = mediaStorageProperties;
    this.mailUsername = mailUsername;
    this.mailHost = mailHost;
    this.appVersion = appVersion;
  }

  public SystemStatusResponse check() {
    List<ComponentStatus> components = new ArrayList<>();
    components.add(checkApiRuntime());
    components.add(checkDatabase());
    components.add(checkRedis());
    components.add(checkPayment());
    components.add(checkMail());
    components.add(checkMediaStorage());
    components.add(checkTranscoder());

    String overall = aggregate(components);
    long uptimeSeconds = ManagementFactory.getRuntimeMXBean().getUptime() / 1000L;
    return new SystemStatusResponse(overall, Instant.now(), uptimeSeconds, appVersion, components);
  }

  private ComponentStatus checkApiRuntime() {
    Runtime runtime = Runtime.getRuntime();
    long usedMb = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
    long maxMb = runtime.maxMemory() / (1024 * 1024);
    String detail = String.format("JVM bộ nhớ %d/%d MB, %d luồng", usedMb, maxMb,
        ManagementFactory.getThreadMXBean().getThreadCount());
    String status = usedMb > maxMb * 0.9 ? STATUS_DEGRADED : STATUS_OPERATIONAL;
    return new ComponentStatus(
        "api",
        "API máy chủ",
        "Spring Boot REST cung cấp dữ liệu phim, người dùng, thanh toán.",
        status,
        detail,
        null);
  }

  private ComponentStatus checkDatabase() {
    long start = System.currentTimeMillis();
    try (Connection conn = dataSource.getConnection()) {
      boolean valid = conn.isValid(2);
      long latency = System.currentTimeMillis() - start;
      if (!valid) {
        return new ComponentStatus("database", "Cơ sở dữ liệu",
            "MySQL lưu trữ dữ liệu người dùng, phim và lịch sử.",
            STATUS_OUTAGE, "Kết nối không phản hồi", latency);
      }
      String detail = String.format("Kết nối %s", conn.getMetaData().getURL().replaceAll("\\?.*", ""));
      String status = latency > 1000 ? STATUS_DEGRADED : STATUS_OPERATIONAL;
      return new ComponentStatus("database", "Cơ sở dữ liệu",
          "MySQL lưu trữ dữ liệu người dùng, phim và lịch sử.",
          status, detail, latency);
    } catch (Exception e) {
      log.warn("Database check failed: {}", e.getMessage());
      long latency = System.currentTimeMillis() - start;
      return new ComponentStatus("database", "Cơ sở dữ liệu",
          "MySQL lưu trữ dữ liệu người dùng, phim và lịch sử.",
          STATUS_OUTAGE, "Không kết nối được tới MySQL", latency);
    }
  }

  private ComponentStatus checkRedis() {
    long start = System.currentTimeMillis();
    try (RedisConnection conn = redisConnectionFactory.getConnection()) {
      String pong = conn.ping();
      long latency = System.currentTimeMillis() - start;
      if (!"PONG".equalsIgnoreCase(pong)) {
        return new ComponentStatus("redis", "Cache Redis",
            "Cache OTP, danh mục phim và phiên xác thực.",
            STATUS_DEGRADED, "Phản hồi bất thường: " + pong, latency);
      }
      String status = latency > 500 ? STATUS_DEGRADED : STATUS_OPERATIONAL;
      return new ComponentStatus("redis", "Cache Redis",
          "Cache OTP, danh mục phim và phiên xác thực.",
          status, "PING/PONG thành công", latency);
    } catch (Exception e) {
      log.warn("Redis check failed: {}", e.getMessage());
      long latency = System.currentTimeMillis() - start;
      return new ComponentStatus("redis", "Cache Redis",
          "Cache OTP, danh mục phim và phiên xác thực.",
          STATUS_OUTAGE, "Không kết nối được tới Redis", latency);
    }
  }

  private ComponentStatus checkPayment() {
    boolean configured = isNotBlank(payOSConfig.getClientId())
        && isNotBlank(payOSConfig.getApiKey())
        && isNotBlank(payOSConfig.getChecksumKey());
    if (!configured) {
      return new ComponentStatus("payment", "Cổng thanh toán PayOS",
          "Tạo mã QR và xác nhận giao dịch cho gói thuê bao.",
          STATUS_MAINTENANCE, "Khoá PayOS chưa cấu hình - tính năng tạm tắt", null);
    }
    return new ComponentStatus("payment", "Cổng thanh toán PayOS",
        "Tạo mã QR và xác nhận giao dịch cho gói thuê bao.",
        STATUS_OPERATIONAL, "Đã cấu hình client-id, api-key và checksum-key", null);
  }

  private ComponentStatus checkMail() {
    boolean configured = isNotBlank(mailUsername) && isNotBlank(mailHost);
    if (!configured) {
      return new ComponentStatus("mail", "Dịch vụ email",
          "Gửi OTP, xác minh tài khoản, biên lai và phản hồi liên hệ.",
          STATUS_MAINTENANCE, "SMTP chưa cấu hình - email tạm dừng", null);
    }
    return new ComponentStatus("mail", "Dịch vụ email",
        "Gửi OTP, xác minh tài khoản, biên lai và phản hồi liên hệ.",
        STATUS_OPERATIONAL, "SMTP " + mailHost, null);
  }

  private ComponentStatus checkMediaStorage() {
    String moviesDir = mediaStorageProperties.getMoviesDataDirectory();
    String hlsDir = mediaStorageProperties.getHlsDirectory();
    String keysDir = mediaStorageProperties.getKeysDirectory();

    boolean moviesOk = isDirectory(moviesDir);
    boolean hlsOk = isDirectory(hlsDir);
    boolean keysOk = isDirectory(keysDir);

    if (moviesOk && hlsOk && keysOk) {
      return new ComponentStatus("storage", "Lưu trữ media",
          "Thư mục phim, HLS và khoá mã hoá phục vụ phát trực tuyến.",
          STATUS_OPERATIONAL, "Tất cả thư mục dữ liệu đều khả dụng", null);
    }
    StringBuilder missing = new StringBuilder();
    if (!moviesOk) missing.append("movies, ");
    if (!hlsOk) missing.append("hls, ");
    if (!keysOk) missing.append("keys, ");
    String trimmed = missing.length() > 2 ? missing.substring(0, missing.length() - 2) : "";
    boolean anyOk = moviesOk || hlsOk || keysOk;
    return new ComponentStatus("storage", "Lưu trữ media",
        "Thư mục phim, HLS và khoá mã hoá phục vụ phát trực tuyến.",
        anyOk ? STATUS_DEGRADED : STATUS_OUTAGE,
        "Thiếu thư mục: " + trimmed,
        null);
  }

  private ComponentStatus checkTranscoder() {
    String ffmpegPath = mediaStorageProperties.getFfmpegPath();
    if (!isNotBlank(ffmpegPath)) {
      return new ComponentStatus("transcoder", "Bộ chuyển mã FFmpeg",
          "Chuyển mã video sang HLS đa chất lượng.",
          STATUS_MAINTENANCE, "Chưa cấu hình đường dẫn ffmpeg", null);
    }
    File ffmpeg = new File(ffmpegPath);
    if (ffmpeg.exists() && ffmpeg.canExecute()) {
      return new ComponentStatus("transcoder", "Bộ chuyển mã FFmpeg",
          "Chuyển mã video sang HLS đa chất lượng.",
          STATUS_OPERATIONAL, "FFmpeg sẵn sàng", null);
    }
    return new ComponentStatus("transcoder", "Bộ chuyển mã FFmpeg",
        "Chuyển mã video sang HLS đa chất lượng.",
        STATUS_DEGRADED, "Không tìm thấy hoặc không thực thi được FFmpeg", null);
  }

  private boolean isDirectory(String path) {
    if (!isNotBlank(path)) return false;
    File f = new File(path);
    return f.exists() && f.isDirectory();
  }

  private boolean isNotBlank(String s) {
    return s != null && !s.isBlank();
  }

  private String aggregate(List<ComponentStatus> components) {
    boolean hasOutage = components.stream().anyMatch(c -> STATUS_OUTAGE.equals(c.status()));
    if (hasOutage) return STATUS_OUTAGE;
    boolean hasDegraded = components.stream().anyMatch(c -> STATUS_DEGRADED.equals(c.status()));
    if (hasDegraded) return STATUS_DEGRADED;
    boolean hasMaintenance = components.stream().anyMatch(c -> STATUS_MAINTENANCE.equals(c.status()));
    if (hasMaintenance) return STATUS_MAINTENANCE;
    return STATUS_OPERATIONAL;
  }
}
