package com.hoaug.movieapi.common.enums;

public enum ErrorCode {
  USER_NOT_FOUND(404, "Không tìm thấy người dùng"),
  WRONG_PASSWORD(400, "Mật khẩu cũ không chính xác"),
  USER_EXISTED(409, "Tên đăng nhập đã tồn tại trong hệ thống"),
  EMAIL_EXISTED(409, "Email này đã được liên kết với một tài khoản khác"),
  INVALID_CREDENTIALS(401, "Tên đăng nhập, email hoặc mật khẩu không chính xác"),
  ACCOUNT_NOT_ACTIVE(403, "Tài khoản của bạn chưa được kích hoạt hoặc đang bị khóa"),
  INVALID_REFRESH_TOKEN(401, "Refresh token không hợp lệ"),
  INVALID_RESET_TOKEN(400, "Token đặt lại mật khẩu không hợp lệ");

  private final int statusCode;
  private final String message;

  ErrorCode(int statusCode, String message) {
    this.statusCode = statusCode;
    this.message = message;
  }

  public int getStatusCode () {
    return statusCode;
  }

  public String getMessage () {
    return message;
  }
}
