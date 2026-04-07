package com.hoaug.movieapi.common.event;

public class UserLoggedInEvent extends DomainEvent {
  private final Long userId;
  private final String deviceName;
  private final String ipAddress;

  public UserLoggedInEvent(Long userId, String deviceName, String ipAddress) {
    this.userId = userId;
    this.deviceName = deviceName;
    this.ipAddress = ipAddress;
  }

  public Long getUserId () {
    return userId;
  }

  public String getDeviceName () {
    return deviceName;
  }

  public String getIpAddress () {
    return ipAddress;
  }
}
