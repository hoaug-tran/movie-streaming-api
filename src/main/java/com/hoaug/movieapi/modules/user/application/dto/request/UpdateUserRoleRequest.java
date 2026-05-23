package com.hoaug.movieapi.modules.user.application.dto.request;

import com.hoaug.movieapi.modules.user.domain.model.Role;

import jakarta.validation.constraints.NotNull;

public class UpdateUserRoleRequest {
  @NotNull(message = "Vui lòng chọn vai trò người dùng.")
  private Role role;

  public Role getRole () {
    return role;
  }

  public void setRole (Role role) {
    this.role = role;
  }
}
