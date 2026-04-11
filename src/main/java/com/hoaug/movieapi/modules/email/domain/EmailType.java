package com.hoaug.movieapi.modules.email.domain;

public enum EmailType {
  FORGOT_PASSWORD("Quên mật khẩu"), RESET_PASSWORD("Xác nhận reset mật khẩu"),
  SIGNUP_SUCCESS("Đăng ký thành công"), EMAIL_VERIFICATION("Xác minh email"),
  ACCOUNT_NOTIFICATION("Thông báo tài khoản"), NEW_MOVIE_RELEASE("Phim mới phát hành");

  private final String displayName;

  EmailType(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName () {
    return displayName;
  }
}
