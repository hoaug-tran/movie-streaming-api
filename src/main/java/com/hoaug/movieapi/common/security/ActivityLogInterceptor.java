package com.hoaug.movieapi.common.security;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.hoaug.movieapi.modules.activitylog.application.service.ActivityLogService;
import com.hoaug.movieapi.modules.activitylog.domain.model.ActivityScope;
import com.hoaug.movieapi.modules.activitylog.domain.model.ActivitySeverity;
import com.hoaug.movieapi.modules.auth.infrastructure.security.JwtService;
import com.hoaug.movieapi.modules.user.domain.model.Role;
import com.hoaug.movieapi.modules.user.infrastructure.persistence.entity.UserEntity;
import com.hoaug.movieapi.modules.user.infrastructure.persistence.repository.JpaUserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ActivityLogInterceptor implements HandlerInterceptor {

  private final ActivityLogService activityLogService;
  private final JpaUserRepository userRepository;
  private final JwtService jwtService;

  public ActivityLogInterceptor(ActivityLogService activityLogService,
      JpaUserRepository userRepository, JwtService jwtService) {
    this.activityLogService = activityLogService;
    this.userRepository = userRepository;
    this.jwtService = jwtService;
  }

  @Override
  public void afterCompletion (HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
    if (ex != null || response.getStatus() < 200 || response.getStatus() >= 300) {
      return;
    }

    String path = request.getRequestURI();
    String method = request.getMethod().toUpperCase();

    String cleanPath = path;
    String prefix = "/api/v1";
    if (cleanPath.startsWith(prefix)) {
      cleanPath = cleanPath.substring(prefix.length());
    }

    String username = null;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()
        && !"anonymousUser".equals(authentication.getName())) {
      username = authentication.getName();
    }

    if (username == null) {
      String authHeader = request.getHeader("Authorization");
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);
        try {
          username = jwtService.extractUsername(token);
        } catch (Exception e) {
          // ignore
        }
      }
    }

    if (username == null && request.getCookies() != null) {
      for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
        if ("accessToken".equals(cookie.getName())) {
          try {
            username = jwtService.extractUsername(cookie.getValue());
          } catch (Exception e) {
            // ignore
          }
          break;
        }
      }
    }

    if (username == null) {
      java.util.Collection<String> cookies = response.getHeaders("Set-Cookie");
      if (cookies != null) {
        for (String cookie : cookies) {
          if (cookie.contains("accessToken=")) {
            String token = extractCookieValue(cookie, "accessToken");
            if (token != null && !token.isEmpty()) {
              try {
                username = jwtService.extractUsername(token);
              } catch (Exception e) {
                // ignore
              }
            }
            break;
          }
        }
      }
    }

    UserEntity user = null;
    if (username != null) {
      Optional<UserEntity> userOpt = userRepository.findByUsername(username);
      if (userOpt.isPresent()) {
        user = userOpt.get();
      }
    }

    String actorName = user != null ? user.getFullName() : "Khách truy cập";
    Long actorId = user != null ? user.getId() : null;

    ActivityScope scope = ActivityScope.USER;
    String action = "";
    String description = "";
    String targetType = "";
    ActivitySeverity severity = ActivitySeverity.INFO;

    if (cleanPath.startsWith("/auth")) {
      targetType = "USER";
      if (cleanPath.contains("/login")) {
        action = "Đăng nhập";
        description = actorName + " đã đăng nhập vào hệ thống.";
        severity = ActivitySeverity.SUCCESS;
      } else if (cleanPath.contains("/register")) {
        action = "Đăng ký";
        description = actorName + " đã đăng ký tài khoản mới.";
        severity = ActivitySeverity.SUCCESS;
      } else if (cleanPath.contains("/forgot-password")) {
        action = "Quên mật khẩu";
        description = "Người dùng yêu cầu mã xác thực đặt lại mật khẩu.";
        severity = ActivitySeverity.WARNING;
      } else if (cleanPath.contains("/reset-password")) {
        action = "Lấy lại mật khẩu";
        description = "Đặt lại mật khẩu tài khoản thành công.";
        severity = ActivitySeverity.SUCCESS;
      } else if (cleanPath.contains("/change-password")) {
        action = "Thay đổi mật khẩu";
        description = actorName + " đã thay đổi mật khẩu tài khoản.";
      } else if (cleanPath.contains("/logout")) {
        action = "Đăng xuất";
        description = actorName + " đã đăng xuất khỏi hệ thống.";
      }
    } else if (cleanPath.startsWith("/admin/episodes")
        || (cleanPath.startsWith("/admin/movies") && cleanPath.contains("/episodes"))) {
      targetType = "EPISODE";
      scope = ActivityScope.ADMIN;
      if ("POST".equals(method)) {
        action = "Thêm tập phim";
        description = actorName + " đã tải lên tập phim mới.";
        severity = ActivitySeverity.SUCCESS;
      } else if ("PUT".equals(method)) {
        action = "Cập nhật tập phim";
        description = actorName + " đã cập nhật thông tin tập phim.";
      } else if ("DELETE".equals(method)) {
        action = "Xóa tập phim";
        description = actorName + " đã xóa tập phim khỏi hệ thống.";
        severity = ActivitySeverity.DANGER;
      }
    } else if (cleanPath.startsWith("/admin/categories")
        || (cleanPath.startsWith("/admin/movies") && cleanPath.contains("/categories"))) {
      targetType = "CATEGORY";
      scope = ActivityScope.ADMIN;
      if ("POST".equals(method)) {
        action = "Thêm thể loại";
        description = actorName + " đã thêm thể loại phim mới.";
        severity = ActivitySeverity.SUCCESS;
      } else if ("PUT".equals(method)) {
        action = "Cập nhật thể loại";
        description = actorName + " đã cập nhật thông tin thể loại phim.";
      } else if ("DELETE".equals(method)) {
        action = "Xóa thể loại";
        description = actorName + " đã xóa thể loại phim.";
        severity = ActivitySeverity.DANGER;
      }
    } else if (cleanPath.startsWith("/admin/tags")
        || (cleanPath.startsWith("/admin/movies") && cleanPath.contains("/tags"))) {
      targetType = "TAG";
      scope = ActivityScope.ADMIN;
      if ("POST".equals(method)) {
        action = "Thêm thẻ";
        description = actorName + " đã tạo thẻ phim mới.";
        severity = ActivitySeverity.SUCCESS;
      } else if ("PUT".equals(method)) {
        action = "Cập nhật thẻ";
        description = actorName + " đã cập nhật thông tin thẻ phim.";
      } else if ("DELETE".equals(method)) {
        action = "Xóa thẻ";
        description = actorName + " đã xóa thẻ phim.";
        severity = ActivitySeverity.DANGER;
      }
    } else if (cleanPath.startsWith("/admin/studios")
        || (cleanPath.startsWith("/admin/movies") && cleanPath.contains("/studios"))) {
      targetType = "STUDIO";
      scope = ActivityScope.ADMIN;
      if ("POST".equals(method)) {
        action = "Thêm hãng sản xuất";
        description = actorName + " đã thêm hãng sản xuất mới.";
        severity = ActivitySeverity.SUCCESS;
      } else if ("PUT".equals(method)) {
        action = "Cập nhật hãng sản xuất";
        description = actorName + " đã cập nhật thông tin hãng sản xuất.";
      } else if ("DELETE".equals(method)) {
        action = "Xóa hãng sản xuất";
        description = actorName + " đã gỡ bỏ hãng sản xuất.";
        severity = ActivitySeverity.DANGER;
      }
    } else if (cleanPath.startsWith("/admin/persons")
        || (cleanPath.startsWith("/admin/movies") && cleanPath.contains("/persons"))) {
      targetType = "PERSON";
      scope = ActivityScope.ADMIN;
      if ("POST".equals(method)) {
        action = "Thêm nhân vật";
        description = actorName + " đã thêm thông tin diễn viên/đạo diễn.";
        severity = ActivitySeverity.SUCCESS;
      } else if ("PUT".equals(method)) {
        action = "Cập nhật nhân vật";
        description = actorName + " đã cập nhật thông tin diễn viên/đạo diễn.";
      } else if ("DELETE".equals(method)) {
        action = "Xóa nhân vật";
        description = actorName + " đã xóa thông tin diễn viên/đạo diễn.";
        severity = ActivitySeverity.DANGER;
      }
    } else if (cleanPath.startsWith("/admin/movies")) {
      targetType = "MOVIE";
      scope = ActivityScope.ADMIN;
      if ("POST".equals(method)) {
        action = "Thêm phim mới";
        description = actorName + " đã thêm phim mới vào hệ thống.";
        severity = ActivitySeverity.SUCCESS;
      } else if ("PUT".equals(method)) {
        action = "Cập nhật phim";
        description = actorName + " đã cập nhật thông tin phim.";
      } else if ("DELETE".equals(method)) {
        action = "Xóa phim";
        description = actorName + " đã xóa phim khỏi hệ thống.";
        severity = ActivitySeverity.DANGER;
      } else if (cleanPath.contains("/status")) {
        action = "Thay đổi trạng thái phim";
        description = actorName + " đã thay đổi trạng thái của phim.";
      }
    } else if (cleanPath.startsWith("/admin/media") || cleanPath.startsWith("/admin/upload")) {
      targetType = "MEDIA";
      scope = ActivityScope.ADMIN;
      if ("POST".equals(method)) {
        action = "Tải lên tệp đa phương tiện";
        description = actorName + " đã tải lên tệp tin đa phương tiện lên hệ thống lưu trữ.";
        severity = ActivitySeverity.SUCCESS;
      }
    } else if (cleanPath.startsWith("/comments")) {
      targetType = "COMMENT";
      if (cleanPath.contains("/admin/")) {
        scope = ActivityScope.ADMIN;
        if ("DELETE".equals(method)) {
          action = "Xóa bình luận (Quản trị)";
          description = actorName + " đã kiểm duyệt và xóa bình luận vi phạm.";
          severity = ActivitySeverity.DANGER;
        }
      } else {
        if ("POST".equals(method)) {
          action = "Gửi bình luận";
          description = actorName + " đã gửi một bình luận mới.";
        } else if ("PUT".equals(method)) {
          action = "Cập nhật bình luận";
          description = actorName + " đã chỉnh sửa nội dung bình luận.";
        } else if ("DELETE".equals(method)) {
          action = "Xóa bình luận";
          description = actorName + " đã xóa bình luận cá nhân.";
          severity = ActivitySeverity.WARNING;
        }
      }
    } else if (cleanPath.startsWith("/reviews")) {
      targetType = "REVIEW";
      if (cleanPath.contains("/admin/")) {
        scope = ActivityScope.ADMIN;
        if ("DELETE".equals(method)) {
          action = "Xóa đánh giá (Quản trị)";
          description = actorName + " đã kiểm duyệt và xóa đánh giá vi phạm.";
          severity = ActivitySeverity.DANGER;
        }
      } else {
        if ("POST".equals(method)) {
          action = "Đăng đánh giá";
          description = actorName + " đã đăng bài đánh giá phim mới.";
        } else if ("PUT".equals(method)) {
          action = "Cập nhật đánh giá";
          description = actorName + " đã sửa đổi bài đánh giá phim.";
        } else if ("DELETE".equals(method)) {
          action = "Xóa đánh giá";
          description = actorName + " đã xóa bài đánh giá cá nhân.";
          severity = ActivitySeverity.WARNING;
        }
      }
    } else if (cleanPath.startsWith("/reports")) {
      targetType = "REPORT";
      if (cleanPath.contains("/resolve")) {
        action = "Xử lý báo cáo";
        description = actorName + " đã xử lý báo cáo vi phạm.";
        scope = ActivityScope.ADMIN;
        severity = ActivitySeverity.SUCCESS;
      } else if ("POST".equals(method)) {
        action = "Báo cáo vi phạm";
        description = actorName + " đã gửi báo cáo vi phạm nội dung.";
        severity = ActivitySeverity.WARNING;
      }
    } else if (cleanPath.startsWith("/users")) {
      if (cleanPath.equals("/users/admin/create") && "POST".equals(method)) {
        targetType = "USER";
        action = "Tạo người dùng";
        description = actorName + " đã tạo tài khoản quản trị/người dùng mới.";
        scope = ActivityScope.ADMIN;
        severity = ActivitySeverity.SUCCESS;
      } else if (cleanPath.endsWith("/status") && "PATCH".equals(method)) {
        targetType = "USER";
        action = "Cập nhật trạng thái người dùng";
        description = actorName + " đã thay đổi trạng thái hoạt động của tài khoản người dùng.";
        scope = ActivityScope.ADMIN;
      } else if (cleanPath.endsWith("/role") && "PATCH".equals(method)) {
        targetType = "USER";
        action = "Cập nhật vai trò người dùng";
        description = actorName + " đã cập nhật quyền hạn vai trò người dùng.";
        scope = ActivityScope.ADMIN;
      } else if (!cleanPath.contains("/me") && "PUT".equals(method)) {
        targetType = "USER";
        action = "Cập nhật người dùng";
        description = actorName + " đã cập nhật thông tin tài khoản người dùng.";
        scope = ActivityScope.ADMIN;
      } else if (!cleanPath.contains("/me") && "DELETE".equals(method)) {
        targetType = "USER";
        action = "Xóa người dùng";
        description = actorName + " đã xóa tài khoản người dùng khỏi hệ thống.";
        scope = ActivityScope.ADMIN;
        severity = ActivitySeverity.DANGER;
      } else if (cleanPath.equals("/users/me") && "PUT".equals(method)) {
        targetType = "USER";
        action = "Cập nhật thông tin cá nhân";
        description = actorName + " đã cập nhật thông tin cá nhân.";
      } else if (cleanPath.equals("/users/me/avatar") && "PATCH".equals(method)) {
        targetType = "USER";
        action = "Cập nhật ảnh đại diện";
        description = actorName + " đã cập nhật ảnh đại diện cá nhân.";
      } else if (cleanPath.equals("/users/me/password") && "PATCH".equals(method)) {
        targetType = "USER";
        action = "Thay đổi mật khẩu";
        description = actorName + " đã thay đổi mật khẩu tài khoản thành công.";
        severity = ActivitySeverity.WARNING;
      } else if (cleanPath.equals("/users/me/email-change/verify-new") && "PATCH".equals(method)) {
        targetType = "USER";
        action = "Thay đổi email";
        description = actorName + " đã xác thực thay đổi địa chỉ email thành công.";
        severity = ActivitySeverity.SUCCESS;
      }
    } else if (cleanPath.startsWith("/advertisements")) {
      targetType = "ADVERTISEMENT";
      scope = ActivityScope.ADMIN;
      if ("POST".equals(method)) {
        action = "Tạo quảng cáo";
        description = actorName + " đã thiết lập chiến dịch quảng cáo mới.";
        severity = ActivitySeverity.SUCCESS;
      } else if ("PUT".equals(method) || "PATCH".equals(method)) {
        action = "Cập nhật quảng cáo";
        description = actorName + " đã cập nhật chiến dịch quảng cáo.";
      } else if ("DELETE".equals(method)) {
        action = "Xóa quảng cáo";
        description = actorName + " đã gỡ bỏ chiến dịch quảng cáo.";
        severity = ActivitySeverity.WARNING;
      }
    } else if (cleanPath.startsWith("/favorites")) {
      targetType = "FAVORITE";
      if ("POST".equals(method)) {
        action = "Yêu thích phim";
        description = actorName + " đã thêm phim vào danh sách yêu thích.";
      } else if ("DELETE".equals(method)) {
        action = "Bỏ yêu thích phim";
        description = actorName + " đã xóa phim khỏi danh sách yêu thích.";
      }
    } else if (cleanPath.startsWith("/subscriptions")) {
      if (cleanPath.startsWith("/subscriptions/plans")) {
        targetType = "SUBSCRIPTION_PLAN";
        scope = ActivityScope.ADMIN;
        if ("POST".equals(method)) {
          action = "Tạo gói dịch vụ";
          description = actorName + " đã tạo gói dịch vụ mới.";
          severity = ActivitySeverity.SUCCESS;
        } else if ("PUT".equals(method)) {
          action = "Cập nhật gói dịch vụ";
          description = actorName + " đã cập nhật thông tin gói dịch vụ.";
        } else if ("DELETE".equals(method)) {
          action = "Xóa gói dịch vụ";
          description = actorName + " đã xóa gói dịch vụ.";
          severity = ActivitySeverity.DANGER;
        }
      } else if (cleanPath.equals("/subscriptions/subscribe")) {
        targetType = "SUBSCRIPTION";
        if ("POST".equals(method)) {
          action = "Đăng ký gói dịch vụ";
          description = actorName + " đã kích hoạt đăng ký gói dịch vụ thành công.";
          severity = ActivitySeverity.SUCCESS;
        }
      } else if (cleanPath.equals("/subscriptions/me/current/auto-renew")) {
        targetType = "SUBSCRIPTION";
        if ("PATCH".equals(method)) {
          action = "Cập nhật tự động gia hạn gói";
          description = actorName + " đã thay đổi thiết lập tự động gia hạn gói dịch vụ.";
        }
      } else if (cleanPath.equals("/subscriptions/admin/assign")) {
        targetType = "SUBSCRIPTION";
        scope = ActivityScope.ADMIN;
        if ("POST".equals(method)) {
          action = "Cấp gói dịch vụ cho người dùng";
          description = actorName
              + " đã chỉ định gán gói dịch vụ thủ công cho tài khoản người dùng.";
          severity = ActivitySeverity.SUCCESS;
        }
      }
    } else if (cleanPath.startsWith("/payments")) {
      targetType = "PAYMENT";
      if (cleanPath.contains("/checkout")) {
        if ("POST".equals(method)) {
          action = "Khởi tạo thanh toán mua gói";
          description = actorName + " đã tạo yêu cầu thanh toán để mua gói dịch vụ.";
        }
      } else if (cleanPath.contains("/verify")) {
        if ("POST".equals(method)) {
          action = "Xác thực giao dịch thanh toán";
          description = actorName + " đã yêu cầu đồng bộ xác minh giao dịch thanh toán.";
        }
      } else if (cleanPath.contains("/success")) {
        if ("GET".equals(method)) {
          action = "Đăng ký gói dịch vụ thành công";
          description = actorName + " đã hoàn tất quy trình thanh toán và nâng cấp gói thành công.";
          severity = ActivitySeverity.SUCCESS;
        }
      }
    } else if (cleanPath.startsWith("/watchlists")) {
      targetType = "WATCHLIST";
      if ("POST".equals(method)) {
        action = "Thêm phim xem sau";
        description = actorName + " đã thêm phim vào danh sách xem sau.";
      } else if ("DELETE".equals(method)) {
        action = "Xóa phim xem sau";
        description = actorName + " đã loại bỏ phim khỏi danh sách xem sau.";
      }
    } else if (cleanPath.startsWith("/watch-histories")) {
      targetType = "WATCH_HISTORY";
      if ("POST".equals(method)) {
        return;
      } else if ("DELETE".equals(method)) {
        if (cleanPath.endsWith("/me")) {
          action = "Xóa toàn bộ lịch sử xem";
          description = actorName + " đã xóa sạch toàn bộ lịch sử xem phim.";
          severity = ActivitySeverity.WARNING;
        } else {
          action = "Xóa một lịch sử xem";
          description = actorName + " đã xóa một phần lịch sử xem phim.";
        }
      }
    } else if (cleanPath.startsWith("/search-histories")) {
      targetType = "SEARCH_HISTORY";
      if ("POST".equals(method)) {
        action = "Tìm kiếm phim";
        description = actorName + " đã thực hiện tìm kiếm từ khóa mới.";
      } else if ("DELETE".equals(method)) {
        if (cleanPath.endsWith("/me")) {
          action = "Xóa toàn bộ lịch sử tìm kiếm";
          description = actorName + " đã xóa toàn bộ lịch sử tìm kiếm từ khóa.";
        } else {
          action = "Xóa một lịch sử tìm kiếm";
          description = actorName + " đã xóa một từ khóa tìm kiếm.";
        }
      }
    } else if (cleanPath.startsWith("/review-likes")) {
      targetType = "REVIEW_LIKE";
      if ("POST".equals(method)) {
        action = "Thích đánh giá";
        description = actorName + " đã thích bài đánh giá phim.";
      } else if ("DELETE".equals(method)) {
        action = "Bỏ thích đánh giá";
        description = actorName + " đã bỏ thích bài đánh giá phim.";
      }
    } else if (cleanPath.startsWith("/comment-likes")) {
      targetType = "COMMENT_LIKE";
      if ("POST".equals(method)) {
        action = "Thích bình luận";
        description = actorName + " đã thích bình luận.";
      } else if ("DELETE".equals(method)) {
        action = "Bỏ thích bình luận";
        description = actorName + " đã bỏ thích bình luận.";
      }
    } else if (cleanPath.startsWith("/device-sessions")) {
      targetType = "DEVICE_SESSION";
      if ("PATCH".equals(method)) {
        if (cleanPath.contains("/revoke-all")) {
          action = "Thu hồi tất cả thiết bị";
          description = actorName + " đã đăng xuất ra khỏi toàn bộ các thiết bị đang hoạt động.";
          severity = ActivitySeverity.DANGER;
        } else if (cleanPath.contains("/revoke")) {
          action = "Thu hồi phiên thiết bị";
          description = actorName + " đã thu hồi một phiên truy cập hoạt động.";
          severity = ActivitySeverity.WARNING;
        }
      }
    } else if (cleanPath.startsWith("/notifications")) {
      targetType = "NOTIFICATION";
      if (cleanPath.contains("/admin")
          || ("POST".equals(method) && cleanPath.equals("/notifications"))) {
        scope = ActivityScope.ADMIN;
        if (cleanPath.contains("/broadcast")) {
          action = "Gửi thông báo toàn hệ thống";
          description = actorName + " đã gửi phát sóng thông báo tới toàn bộ người dùng.";
          severity = ActivitySeverity.SUCCESS;
        } else if ("POST".equals(method)) {
          action = "Tạo thông báo";
          description = actorName + " đã tạo thông báo mới cho người dùng.";
          severity = ActivitySeverity.SUCCESS;
        } else if ("PUT".equals(method)) {
          action = "Cập nhật thông báo";
          description = actorName + " đã cập nhật nội dung thông báo.";
        } else if ("DELETE".equals(method)) {
          action = "Xóa thông báo";
          description = actorName + " đã xóa thông báo khỏi hệ thống.";
          severity = ActivitySeverity.DANGER;
        }
      } else {
        if (cleanPath.endsWith("/read")) {
          action = "Đánh dấu đã đọc thông báo";
          description = actorName + " đã xem thông báo.";
        } else if (cleanPath.endsWith("/read-all")) {
          action = "Đánh dấu tất cả đã đọc";
          description = actorName + " đã đánh dấu đọc toàn bộ thông báo.";
        } else if ("DELETE".equals(method)) {
          if (cleanPath.endsWith("/me")) {
            action = "Xóa toàn bộ thông báo";
            description = actorName + " đã xóa sạch hộp thư thông báo cá nhân.";
            severity = ActivitySeverity.WARNING;
          } else {
            action = "Xóa thông báo";
            description = actorName + " đã xóa một thông báo cá nhân.";
          }
        }
      }
    }

    if (!action.isEmpty()) {
      if (user != null && Role.ROLE_ADMIN.equals(user.getRole())) {
        scope = ActivityScope.ADMIN;
      }
      activityLogService.record(scope, actorId, actorName, action, targetType, null, null,
          description, severity);
    }
  }

  private String extractCookieValue (String header, String cookieName) {
    if (header == null)
      return null;
    String prefix = cookieName + "=";
    int start = header.indexOf(prefix);
    if (start == -1)
      return null;
    start += prefix.length();
    int end = header.indexOf(";", start);
    if (end == -1) {
      return header.substring(start).trim();
    }
    return header.substring(start, end).trim();
  }
}
