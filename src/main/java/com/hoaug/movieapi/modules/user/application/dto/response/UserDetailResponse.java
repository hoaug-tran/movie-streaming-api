package com.hoaug.movieapi.modules.user.application.dto.response;

public class UserDetailResponse extends UserProfileResponse {

  private String currentPlanCode;
  private String currentPlanName;

  public String getCurrentPlanCode() {
    return currentPlanCode;
  }

  public void setCurrentPlanCode(String currentPlanCode) {
    this.currentPlanCode = currentPlanCode;
  }

  public String getCurrentPlanName() {
    return currentPlanName;
  }

  public void setCurrentPlanName(String currentPlanName) {
    this.currentPlanName = currentPlanName;
  }

}
