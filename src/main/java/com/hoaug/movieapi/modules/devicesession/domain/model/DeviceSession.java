package com.hoaug.movieapi.modules.devicesession.domain.model;

import java.time.LocalDateTime;

public class DeviceSession {

  private Long id;
  private Long userId;
  private String deviceName;
  private String deviceType;
  private String ipAddress;
  private String userAgent;
  private LocalDateTime lastActiveAt;
  private LocalDateTime createdAt;
  private Boolean isRevoked;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

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

  public void setCreatedAt (LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Boolean getIsRevoked () {
    return isRevoked;
  }

  public void setIsRevoked (Boolean revoked) {
    isRevoked = revoked;
  }
}