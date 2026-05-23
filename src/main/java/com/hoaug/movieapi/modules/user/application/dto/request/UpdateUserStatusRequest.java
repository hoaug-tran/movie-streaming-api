package com.hoaug.movieapi.modules.user.application.dto.request;

import com.hoaug.movieapi.modules.user.domain.model.AccountStatus;

import jakarta.validation.constraints.NotNull;

public class UpdateUserStatusRequest {
  @NotNull(message = "Vui lòng chọn trạng thái tài khoản.")
  private AccountStatus accountStatus;

  public AccountStatus getAccountStatus () {
    return accountStatus;
  }

  public void setAccountStatus (AccountStatus accountStatus) {
    this.accountStatus = accountStatus;
  }
}
