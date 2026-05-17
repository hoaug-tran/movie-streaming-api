package com.hoaug.movieapi.modules.devicesession.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import com.hoaug.movieapi.common.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "device_sessions")
public class DeviceSessionEntity extends BaseEntity {

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "device_name", length = 100)
  private String deviceName;

  @Column(name = "device_type", length = 50)
  private String deviceType;

  @Column(name = "ip_address", length = 45)
  private String ipAddress;

  @Column(name = "user_agent", length = 500)
  private String userAgent;

  @Column(name = "last_active_at", nullable = false)
  private LocalDateTime lastActiveAt;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "is_revoked", nullable = false)
  private Boolean isRevoked;

  @Column(name = "is_streaming", nullable = false)
  private Boolean isStreaming = false;

  @Column(name = "stream_expires_at")
  private LocalDateTime streamExpiresAt;

  public Long getUserId () {
    return userId;
  }

  public void setUserId (Long userId) {
    this.userId = userId;
  }

  public String getDeviceName () {
    return deviceName;
  }

  public void setDeviceName (String deviceName) {
    this.deviceName = deviceName;
  }

  public String getDeviceType () {
    return deviceType;
  }

  public void setDeviceType (String deviceType) {
    this.deviceType = deviceType;
  }

  public String getIpAddress () {
    return ipAddress;
  }

  public void setIpAddress (String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public String getUserAgent () {
    return userAgent;
  }

  public void setUserAgent (String userAgent) {
    this.userAgent = userAgent;
  }

  public LocalDateTime getLastActiveAt () {
    return lastActiveAt;
  }

  public void setLastActiveAt (LocalDateTime lastActiveAt) {
    this.lastActiveAt = lastActiveAt;
  }

  public LocalDateTime getCreatedAt () {
    return createdAt;
  }

  @PrePersist
  protected void prePersist () {
    if (createdAt == null) createdAt = LocalDateTime.now();
    if (lastActiveAt == null) lastActiveAt = LocalDateTime.now();
  }

  public Boolean getIsRevoked () {
    return isRevoked;
  }

  public void setIsRevoked (Boolean revoked) {
    isRevoked = revoked;
  }

  public Boolean getIsStreaming () {
    return isStreaming;
  }

  public void setIsStreaming (Boolean streaming) {
    isStreaming = streaming;
  }

  public LocalDateTime getStreamExpiresAt () {
    return streamExpiresAt;
  }

  public void setStreamExpiresAt (LocalDateTime streamExpiresAt) {
    this.streamExpiresAt = streamExpiresAt;
  }
}