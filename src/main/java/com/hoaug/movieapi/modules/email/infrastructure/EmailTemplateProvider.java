package com.hoaug.movieapi.modules.email.infrastructure;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.email.domain.EmailType;

@Component
public class EmailTemplateProvider {

  public String getTemplate (EmailType emailType, Map<String, String> variables) {
    return switch (emailType) {
    case FORGOT_PASSWORD -> getForgotPasswordTemplate(variables);
    case RESET_PASSWORD -> getResetPasswordTemplate(variables);
    case SIGNUP_SUCCESS -> getSignupSuccessTemplate(variables);
    case EMAIL_VERIFICATION -> getEmailVerificationTemplate(variables);
    case ACCOUNT_NOTIFICATION -> getAccountNotificationTemplate(variables);
    case NEW_MOVIE_RELEASE -> getNewMovieReleaseTemplate(variables);
    case OTP_VERIFICATION -> getOtpVerificationTemplate(variables);
    };
  }

  private String getForgotPasswordTemplate (Map<String, String> variables) {
    String fullName = variables.getOrDefault("fullName", "Người dùng");
    String resetLink = variables.getOrDefault("resetLink", "#");
    return "<!DOCTYPE html><html lang='vi'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'><title>Yêu cầu đặt lại mật khẩu</title><style>body{font-family:-apple-system,BlinkMacSystemFont,'Segoe UI','Roboto','Helvetica Neue',Arial,sans-serif;background-color:#f8f9fa;margin:0;padding:0}.container{max-width:600px;margin:0 auto;padding:20px}.email-wrapper{background-color:#fff;border-radius:8px;overflow:hidden;box-shadow:0 2px 8px rgba(0,0,0,.08)}.header{background-color:#1a1a1a;padding:40px 20px;text-align:center}.header h1{color:#fff;margin:0;font-size:24px;font-weight:600;letter-spacing:.5px}.content{padding:40px 30px;color:#2c2c2c}.greeting{font-size:16px;margin-bottom:24px;line-height:1.6}.message{font-size:14px;color:#555;line-height:1.8;margin-bottom:32px}.cta-button{display:inline-block;background-color:#1a1a1a;color:#fff;padding:12px 32px;border-radius:4px;text-decoration:none;font-size:14px;font-weight:600}.footer{background-color:#f8f9fa;padding:24px 30px;border-top:1px solid #e0e0e0;font-size:12px;color:#777}</style></head><body><div class='container'><div class='email-wrapper'><div class='header'><h1>Gió Phim</h1></div><div class='content'><div class='greeting'>Xin chào <strong>"
        + fullName
        + "</strong>,</div><div class='message'>Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn. Nếu đây là yêu cầu của bạn, vui lòng nhấp vào nút dưới đây để tạo mật khẩu mới.<br><br><strong>Liên kết này sẽ hết hạn trong 1 giờ.</strong></div><a href='"
        + resetLink
        + "' class='cta-button'>Đặt lại mật khẩu</a></div><div class='footer'><div>© 2026 Gió Phim. All rights reserved.</div></div></div></div></body></html>";
  }

  private String getResetPasswordTemplate (Map<String, String> variables) {
    String fullName = variables.getOrDefault("fullName", "Người dùng");
    return "<!DOCTYPE html><html lang='vi'><head><meta charset='UTF-8'><title>Mật khẩu đã được đặt lại</title><style>body{font-family:-apple-system,BlinkMacSystemFont,'Segoe UI','Roboto','Helvetica Neue',Arial,sans-serif;background-color:#f8f9fa}.container{max-width:600px;margin:0 auto;padding:20px}.email-wrapper{background-color:#fff;border-radius:8px;overflow:hidden;box-shadow:0 2px 8px rgba(0,0,0,.08)}.header{background-color:#1a1a1a;padding:40px 20px;text-align:center}.header h1{color:#fff;margin:0;font-size:24px;font-weight:600}.content{padding:40px 30px;color:#2c2c2c;text-align:center}.greeting{font-size:18px;font-weight:600;margin-bottom:16px}.message{font-size:14px;color:#555;line-height:1.8;margin-bottom:32px}.info-box{background-color:#f8f9fa;border-left:4px solid #1a1a1a;padding:16px;margin:24px 0;text-align:left;font-size:13px;color:#555}.footer{background-color:#f8f9fa;padding:24px 30px;border-top:1px solid #e0e0e0;font-size:12px;color:#777;text-align:center}</style></head><body><div class='container'><div class='email-wrapper'><div class='header'><h1>Gió Phim</h1></div><div class='content'><div style='font-size:48px;margin:20px 0'>✓</div><div class='greeting'>Mật khẩu của bạn đã được đặt lại thành công</div><div class='message'>Xin chào <strong>"
        + fullName
        + "</strong>,<br><br>Mật khẩu của bạn đã được cập nhật thành công. Bạn có thể đăng nhập lại bằng mật khẩu mới.</div><div class='info-box'><strong>Lưu ý bảo mật:</strong> Nếu bạn không thực hiện thay đổi này, vui lòng liên hệ với chúng tôi ngay.</div></div><div class='footer'><div>© 2026 Gió Phim. All rights reserved.</div></div></div></div></body></html>";
  }

  private String getSignupSuccessTemplate (Map<String, String> variables) {
    String fullName = variables.getOrDefault("fullName", "Người dùng");
    String verificationLink = variables.getOrDefault("verificationLink", "#");
    return "<!DOCTYPE html><html lang='vi'><head><meta charset='UTF-8'><title>Chào mừng đến với Gió Phim</title><style>body{font-family:-apple-system,BlinkMacSystemFont,'Segoe UI','Roboto','Helvetica Neue',Arial,sans-serif;background-color:#f8f9fa}.container{max-width:600px;margin:0 auto;padding:20px}.email-wrapper{background-color:#fff;border-radius:8px;overflow:hidden;box-shadow:0 2px 8px rgba(0,0,0,.08)}.header{background-color:#1a1a1a;padding:40px 20px;text-align:center}.header h1{color:#fff;margin:0;font-size:28px;font-weight:600}.content{padding:40px 30px;color:#2c2c2c}.greeting{font-size:18px;font-weight:600;margin-bottom:24px}.message{font-size:14px;color:#555;line-height:1.8;margin-bottom:24px}.features{margin:32px 0}.feature-item{padding:12px 0;font-size:14px;color:#555;border-bottom:1px solid #f0f0f0}.cta-button{display:inline-block;background-color:#1a1a1a;color:#fff;padding:12px 32px;border-radius:4px;text-decoration:none;font-size:14px;font-weight:600;margin-top:24px}.footer{background-color:#f8f9fa;padding:24px 30px;border-top:1px solid #e0e0e0;font-size:12px;color:#777;text-align:center}</style></head><body><div class='container'><div class='email-wrapper'><div class='header'><h1>Gió Phim</h1></div><div class='content'><div class='greeting'>Chào mừng <strong>"
        + fullName
        + "</strong>!</div><div class='message'>Tài khoản của bạn đã được tạo thành công. Bạn đã sẵn sàng khám phá một thế giới phim ảnh vô tận.</div><div class='features'><div class='feature-item'>• Xem phim trực tuyến với chất lượng cao</div><div class='feature-item'>• Thêm phim yêu thích vào danh sách cá nhân</div><div class='feature-item'>• Nhận thông báo về phim mới phát hành</div><div class='feature-item'>• Đánh giá và nhận xét những bộ phim</div></div><div class='message' style='margin-top:32px'>Xác minh email để kích hoạt tất cả các tính năng.</div><a href='"
        + verificationLink
        + "' class='cta-button'>Xác minh Email</a></div><div class='footer'><div>© 2026 Gió Phim. All rights reserved.</div></div></div></div></body></html>";
  }

  private String getEmailVerificationTemplate (Map<String, String> variables) {
    String fullName = variables.getOrDefault("fullName", "Người dùng");
    String verificationLink = variables.getOrDefault("verificationLink", "#");
    return "<!DOCTYPE html><html lang='vi'><head><meta charset='UTF-8'><title>Xác minh email của bạn</title><style>body{font-family:-apple-system,BlinkMacSystemFont,'Segoe UI','Roboto','Helvetica Neue',Arial,sans-serif;background-color:#f8f9fa}.container{max-width:600px;margin:0 auto;padding:20px}.email-wrapper{background-color:#fff;border-radius:8px;overflow:hidden;box-shadow:0 2px 8px rgba(0,0,0,.08)}.header{background-color:#1a1a1a;padding:40px 20px;text-align:center}.header h1{color:#fff;margin:0;font-size:24px;font-weight:600}.content{padding:40px 30px;color:#2c2c2c}.greeting{font-size:16px;margin-bottom:24px}.message{font-size:14px;color:#555;line-height:1.8;margin-bottom:32px}.cta-button{display:inline-block;background-color:#1a1a1a;color:#fff;padding:12px 32px;border-radius:4px;text-decoration:none;font-size:14px;font-weight:600}.footer{background-color:#f8f9fa;padding:24px 30px;border-top:1px solid #e0e0e0;font-size:12px;color:#777}</style></head><body><div class='container'><div class='email-wrapper'><div class='header'><h1>Gió Phim</h1></div><div class='content'><div class='greeting'>Xin chào <strong>"
        + fullName
        + "</strong>,</div><div class='message'>Cảm ơn bạn đã đăng ký. Vui lòng xác minh email để hoàn tất đăng ký.</div><a href='"
        + verificationLink
        + "' class='cta-button'>Xác minh Ngay</a><div class='message' style='margin-top:32px;font-size:12px;color:#999'>Liên kết hết hạn trong 24 giờ.</div></div><div class='footer'><div>© 2026 Gió Phim. All rights reserved.</div></div></div></div></body></html>";
  }

  private String getAccountNotificationTemplate (Map<String, String> variables) {
    String fullName = variables.getOrDefault("fullName", "Người dùng");
    String notificationMessage = variables.getOrDefault("notificationMessage",
        "Bạn có một thông báo mới.");
    return "<!DOCTYPE html><html lang='vi'><head><meta charset='UTF-8'><title>Thông báo từ Gió phim</title><style>body{font-family:-apple-system,BlinkMacSystemFont,'Segoe UI','Roboto','Helvetica Neue',Arial,sans-serif;background-color:#f8f9fa}.container{max-width:600px;margin:0 auto;padding:20px}.email-wrapper{background-color:#fff;border-radius:8px;overflow:hidden;box-shadow:0 2px 8px rgba(0,0,0,.08)}.header{background-color:#1a1a1a;padding:40px 20px;text-align:center}.header h1{color:#fff;margin:0;font-size:24px;font-weight:600}.content{padding:40px 30px;color:#2c2c2c}.greeting{font-size:16px;margin-bottom:24px}.notification-box{background-color:#f8f9fa;border-left:4px solid #1a1a1a;padding:20px;margin:24px 0}.message{font-size:14px;color:#555;line-height:1.8}.footer{background-color:#f8f9fa;padding:24px 30px;border-top:1px solid #e0e0e0;font-size:12px;color:#777}</style></head><body><div class='container'><div class='email-wrapper'><div class='header'><h1>Gió Phim</h1></div><div class='content'><div class='greeting'>Xin chào <strong>"
        + fullName + "</strong>,</div><div class='notification-box'><div class='message'>"
        + notificationMessage
        + "</div></div></div><div class='footer'><div>© 2026 Gió Phim. All rights reserved.</div></div></div></div></body></html>";
  }

  private String getNewMovieReleaseTemplate (Map<String, String> variables) {
    String fullName = variables.getOrDefault("fullName", "Người dùng");
    String movieTitle = variables.getOrDefault("movieTitle", "Phim");
    String moviePosterUrl = variables.getOrDefault("moviePosterUrl", "");
    String movieLink = variables.getOrDefault("movieLink", "#");
    String posterHtml = moviePosterUrl.isEmpty() ? ""
        : "<img src='" + moviePosterUrl + "' alt='" + movieTitle
            + "' style='max-width:300px;width:100%;border-radius:8px;margin-bottom:24px'>";
    return "<!DOCTYPE html><html lang='vi'><head><meta charset='UTF-8'><title>Phim mới từ Gió Phim</title><style>body{font-family:-apple-system,BlinkMacSystemFont,'Segoe UI','Roboto','Helvetica Neue',Arial,sans-serif;background-color:#f8f9fa}.container{max-width:600px;margin:0 auto;padding:20px}.email-wrapper{background-color:#fff;border-radius:8px;overflow:hidden;box-shadow:0 2px 8px rgba(0,0,0,.08)}.header{background-color:#1a1a1a;padding:40px 20px;text-align:center}.header h1{color:#fff;margin:0;font-size:24px;font-weight:600}.content{padding:40px 30px;color:#2c2c2c}.greeting{font-size:16px;margin-bottom:32px}.movie-container{text-align:center}.movie-title{font-size:22px;font-weight:600;margin:24px 0;color:#1a1a1a}.movie-description{font-size:14px;color:#555;line-height:1.8;margin-bottom:32px}.cta-button{display:inline-block;background-color:#1a1a1a;color:#fff;padding:12px 32px;border-radius:4px;text-decoration:none;font-size:14px;font-weight:600}.footer{background-color:#f8f9fa;padding:24px 30px;border-top:1px solid #e0e0e0;font-size:12px;color:#777;text-align:center}</style></head><body><div class='container'><div class='email-wrapper'><div class='header'><h1>Gió Phim</h1></div><div class='content'><div class='greeting'>Xin chào <strong>"
        + fullName + "</strong>,</div><div class='movie-container'>" + posterHtml
        + "<div class='movie-title'>" + movieTitle
        + "</div><div class='movie-description'>Bộ phim mới được phát hành. Hãy xem ngay!</div><a href='"
        + movieLink
        + "' class='cta-button'>Xem Phim</a></div></div><div class='footer'><div>© 2026 Gió Phim. All rights reserved.</div></div></div></div></body></html>";
  }

  private String getOtpVerificationTemplate (Map<String, String> variables) {
    String fullName = variables.getOrDefault("fullName", "Người dùng");
    String otp = variables.getOrDefault("otp", "------");
    String purposeLabel = variables.getOrDefault("purposeLabel", "xác thực tài khoản");
    String ttlMinutes = variables.getOrDefault("ttlMinutes", "10");
    return "<!DOCTYPE html><html lang='vi'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'><meta name='color-scheme' content='light dark'><meta name='supported-color-schemes' content='light dark'><title>Mã xác thực OTP</title><style>body{font-family:-apple-system,BlinkMacSystemFont,'Segoe UI','Roboto','Helvetica Neue',Arial,sans-serif;background-color:#f4f5f7;margin:0;padding:0;color:#2c2c2c}.container{max-width:600px;margin:0 auto;padding:24px}.email-wrapper{background-color:#fff;border-radius:12px;overflow:hidden;box-shadow:0 4px 16px rgba(20,20,40,.06)}.header{background:linear-gradient(135deg,#0f172a 0%,#1e293b 100%);padding:48px 24px;text-align:center}.brand{color:#fff;margin:0;font-size:26px;font-weight:700;letter-spacing:1px}.tagline{color:#94a3b8;margin-top:8px;font-size:13px;letter-spacing:.5px;text-transform:uppercase}.content{padding:40px 36px}.greeting{font-size:16px;margin-bottom:20px;line-height:1.6}.message{font-size:14px;color:#475569;line-height:1.7;margin-bottom:24px}.otp-box{background:linear-gradient(135deg,#f8fafc 0%,#eef2ff 100%);border:1px solid #e2e8f0;border-radius:12px;padding:28px 16px;text-align:center;margin:32px 0}.otp-label{font-size:12px;color:#64748b;text-transform:uppercase;letter-spacing:2px;margin-bottom:12px}.otp-code{font-family:'SFMono-Regular',Consolas,'Liberation Mono',Menlo,monospace;font-size:36px;font-weight:700;color:#0f172a;letter-spacing:12px;padding:8px 0}.otp-meta{font-size:12px;color:#94a3b8;margin-top:14px}.warning{background-color:#fef9c3;border-left:4px solid #facc15;padding:14px 18px;border-radius:6px;font-size:13px;color:#713f12;margin-top:24px;line-height:1.6}.footer{background-color:#f8fafc;padding:24px 36px;border-top:1px solid #e2e8f0;font-size:12px;color:#94a3b8;text-align:center;line-height:1.6}.footer a{color:#475569;text-decoration:none}@media (prefers-color-scheme: dark){body{background-color:#0f172a!important;color:#f1f5f9!important}.email-wrapper{background-color:#1e293b!important;box-shadow:none!important}.content{color:#e2e8f0!important}.message{color:#cbd5e1!important}.otp-box{background:#334155!important;border-color:#475569!important}.otp-label{color:#94a3b8!important}.otp-code{color:#ffffff!important}.warning{background-color:#422006!important;border-color:#ca8a04!important;color:#fef08a!important}.footer{background-color:#0f172a!important;border-top-color:#334155!important;color:#64748b!important}.footer a{color:#cbd5e1!important}}</style></head><body><div class='container'><div class='email-wrapper'><div class='header'><div class='brand'>Gió Phim</div><div class='tagline'>Xác thực bảo mật</div></div><div class='content'><div class='greeting'>Xin chào <strong>"
        + fullName
        + "</strong>,</div><div class='message'>Bạn đã yêu cầu mã xác thực để <strong>"
        + purposeLabel
        + "</strong>. Vui lòng nhập mã bên dưới để hoàn tất quá trình.</div><div class='otp-box'><div class='otp-label'>Mã xác thực của bạn</div><div class='otp-code'>"
        + otp
        + "</div><div class='otp-meta'>Hiệu lực trong "
        + ttlMinutes
        + " phút</div></div><div class='warning'><strong>Lưu ý bảo mật:</strong> Tuyệt đối không chia sẻ mã này với bất kỳ ai. Đội ngũ Gió Phim sẽ không bao giờ chủ động yêu cầu mã OTP qua điện thoại hoặc email.</div><div class='message' style='margin-top:24px;font-size:13px;color:#94a3b8'>Nếu bạn không thực hiện yêu cầu này, bạn có thể bỏ qua email này một cách an toàn.</div></div><div class='footer'><div>© 2026 Gió Phim. All rights reserved.</div><div style='margin-top:6px'>Email tự động, vui lòng không trả lời.</div></div></div></div></body></html>";
  }
}
