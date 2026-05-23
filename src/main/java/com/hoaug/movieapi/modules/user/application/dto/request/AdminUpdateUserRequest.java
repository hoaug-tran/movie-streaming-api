package com.hoaug.movieapi.modules.user.application.dto.request;

import java.time.LocalDateTime;

import com.hoaug.movieapi.common.validator.ValidSafeString;
import com.hoaug.movieapi.modules.user.domain.model.AccountStatus;
import com.hoaug.movieapi.modules.user.domain.model.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AdminUpdateUserRequest {
  @NotBlank(message = "Vui lòng nhập email.")
  @Email(message = "Email không đúng định dạng.")
  @Size(max = 100, message = "Email tối đa 100 ký tự.")
  private String email;

  @NotBlank(message = "Vui lòng nhập họ tên.")
  @Size(min = 1, max = 100, message = "Họ tên phải từ 1 đến 100 ký tự.")
  @ValidSafeString(minLength = 1, maxLength = 100)
  private String fullName;

  @Size(max = 500, message = "Đường dẫn ảnh đại diện tối đa 500 ký tự.")
  @ValidSafeString(minLength = 0, maxLength = 500)
  private String avatarUrl;

  @NotNull(message = "Vui lòng chọn vai trò.")
  private Role role;

  @NotNull(message = "Vui lòng chọn trạng thái tài khoản.")
  private AccountStatus accountStatus;

  private LocalDateTime premiumExpiryDate;

  public String getEmail () {
    return email;
  }

  public void setEmail (String email) {
    this.email = email;
  }

  public String getFullName () {
    return fullName;
  }

  public void setFullName (String fullName) {
    this.fullName = fullName;
  }

  public String getAvatarUrl () {
    return avatarUrl;
  }

  public void setAvatarUrl (String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  public Role getRole () {
    return role;
  }

  public void setRole (Role role) {
    this.role = role;
  }

  public AccountStatus getAccountStatus () {
    return accountStatus;
  }

  public void setAccountStatus (AccountStatus accountStatus) {
    this.accountStatus = accountStatus;
  }

  public LocalDateTime getPremiumExpiryDate () {
    return premiumExpiryDate;
  }

  public void setPremiumExpiryDate (LocalDateTime premiumExpiryDate) {
    this.premiumExpiryDate = premiumExpiryDate;
  }
}
