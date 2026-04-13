package com.hoaug.movieapi.modules.devicesession.application.dto.request;

import com.hoaug.movieapi.common.validator.ValidSafeString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateDeviceSessionRequest {

  @NotBlank(message = "Device name is required")
  @Size(min = 1, max = 255, message = "Device name must be between 1 and 255 characters")
  @ValidSafeString(minLength = 1, maxLength = 255)
  private String deviceName;

  @NotBlank(message = "Device type is required")
  @Size(min = 1, max = 100, message = "Device type must be between 1 and 100 characters")
  @ValidSafeString(minLength = 1, maxLength = 100)
  private String deviceType;

  @NotBlank(message = "IP address is required")
  @Size(min = 1, max = 50, message = "IP address must be between 1 and 50 characters")
  @ValidSafeString(minLength = 1, maxLength = 50)
  private String ipAddress;

  @NotBlank(message = "User agent is required")
  @Size(min = 1, max = 2048, message = "User agent must be between 1 and 2048 characters")
  @ValidSafeString(minLength = 1, maxLength = 2048)
  private String userAgent;

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
}