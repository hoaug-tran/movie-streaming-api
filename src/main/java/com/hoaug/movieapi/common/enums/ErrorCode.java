package com.hoaug.movieapi.common.enums;

public enum ErrorCode {
  USER_NOT_FOUND(404, "Không tìm thấy người dùng"),
  WRONG_PASSWORD(400, "Mật khẩu cũ không chính xác"),
  USER_EXISTED(409, "Tên đăng nhập đã tồn tại trong hệ thống"),
  EMAIL_EXISTED(409, "Email này đã được liên kết với một tài khoản khác"),
  INVALID_CREDENTIALS(401, "Tên đăng nhập, email hoặc mật khẩu không chính xác"),
  ACCOUNT_NOT_ACTIVE(403, "Tài khoản của bạn chưa được kích hoạt hoặc đang bị khóa"),
  INVALID_REFRESH_TOKEN(401, "Refresh token không hợp lệ"),
  INVALID_RESET_TOKEN(400, "Token đặt lại mật khẩu không hợp lệ"),
  MOVIE_NOT_FOUND(404, "Không tìm thấy bộ phim này"),
  MOVIE_SLUG_EXISTED(409, "Đường dẫn phim (slug) này đã tồn tại."),
  CATEGORY_NOT_FOUND(404, "Không tìm thấy danh mục phim này"),
  TAG_NOT_FOUND(404, "Không tìm thấy thẻ phim này"),
  PERSON_NOT_FOUND(404, "Không tìm thấy người này"),
  STUDIO_NOT_FOUND(404, "Không tìm thấy hãng sản xuất này"),
  EPISODE_NOT_FOUND(404, "Không tìm thấy tập phim này"),
  INVALID_EPISODE_STATUS(400, "Trạng thái tập phim không hợp lệ"),
  COMMENT_NOT_FOUND(404, "Bình luận không tồn tại"),
  COMMENT_LIKE_NOT_FOUND(404, "Bạn chưa thích bình luận này"),
  REVIEW_LIKE_NOT_FOUND(404, "Bạn chưa thích đánh giá này"),
  INVALID_COMMENT_PARENT(400, "Bình luận cấp cha không hợp lệ hoặc không cùng phim"),
  FORBIDDEN(403, "Bạn không có quyền thực hiện thao tác này"),
  UNAUTHORIZED(401, "Bạn không được phép truy cập tài nguyên này"),
  COMMENT_NOT_EDITABLE(400, "Bình luận này hiện không thể chỉnh sửa hoặc phản hồi"),
  REVIEW_NOT_FOUND(404, "Không tìm thấy đánh giá này"),
  VALIDATION_ERROR(400, "Lỗi xác thực dữ liệu"),
  RATE_LIMIT_EXCEEDED(429, "Bạn đang gửi qúa nhiều yêu cầu. Vui lòng chờ một chút"),
  INVALID_REPORT_TARGET(400, "Đối tượng báo cáo không hợp lệ hoặc không tồn tại"),
  REPORT_NOT_FOUND(404, "Yêu cầu báo cáo không tồn tại"),
  INVALID_REPORT_STATUS(400, "Trạng thái báo cáo không hợp lệ"),
  SUBSCRIPTION_PLAN_CODE_EXISTS(4001, "Mã gói đăng ký đã tồn tại"),
  SUBSCRIPTION_PLAN_NOT_FOUND(4002, "Gói đăng ký không tồn tại"),
  SUBSCRIPTION_PLAN_INACTIVE(4003, "Gói đăng ký này hiện đã ngừng hoạt động hoặc tạm ẩn"),
  USER_SUBSCRIPTION_NOT_FOUND(4004,
      "Người dùng chưa đăng ký hoặc không có gói dịch vụ nào còn hiệu lực"),
  PAYMENT_TRANSACTION_NOT_FOUND(5001,
      "Giao dịch thanh toán không tồn tại hoặc mã hóa đơn không hợp lệ"),
  PAYMENT_NOT_SUCCESS(5002, "Giao dịch thanh toán chưa hoàn tất hoặc thất bại"),
  INVOICE_ALREADY_EXISTS(5003, "Hóa đơn cho giao dịch này đã được khởi tạo trước đó"),
  NOTIFICATION_NOT_FOUND(6001, "Thông báo không tồn tại hoặc đã bị xóa"),
  MOVIE_RECOMMENDATION_NOT_FOUND(404, "Không tìm thấy đề xuất phim nào cho người dùng này"),
  DEVICE_SESSION_NOT_FOUND(7001, "Phiên đăng nhập trên thiết bị không tồn tại hoặc đã hết hạn"),
  SEARCH_HISTORY_NOT_FOUND(7002, "Lịch sử tìm kiếm không tồn tại hoặc đã bị xóa"),
  ADVERTISEMENT_NOT_FOUND(404, "Không tìm thấy quảng cáo nào"),
  ADVERTISEMENT_VIEW_NOT_FOUND(404, "Không tìm thấy lượt xem quảng cáo nào");

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
