package com.hoaug.movieapi.modules.dashboard.application.usecase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hoaug.movieapi.modules.advertisement.infrastructure.persistence.entity.AdvertisementEntity;
import com.hoaug.movieapi.modules.advertisement.infrastructure.persistence.repository.JpaAdvertisementRepository;
import com.hoaug.movieapi.modules.comment.domain.model.CommentStatus;
import com.hoaug.movieapi.modules.comment.infrastructure.persistence.repository.JpaCommentRepository;
import com.hoaug.movieapi.modules.dashboard.application.dto.response.DashboardSummaryResponse;
import com.hoaug.movieapi.modules.movie.domain.model.MovieStatus;
import com.hoaug.movieapi.modules.movie.domain.model.MovieType;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaEpisodeRepository;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaMovieRepository;
import com.hoaug.movieapi.modules.report.domain.model.ReportStatus;
import com.hoaug.movieapi.modules.report.infrastructure.persistence.repository.JpaReportRepository;
import com.hoaug.movieapi.modules.review.infrastructure.persistence.repository.JpaReviewRepository;
import com.hoaug.movieapi.modules.subscription.domain.model.PaymentStatus;
import com.hoaug.movieapi.modules.subscription.domain.model.SubscriptionStatus;
import com.hoaug.movieapi.modules.subscription.infrastructure.persistence.repository.JpaPaymentTransactionRepository;
import com.hoaug.movieapi.modules.subscription.infrastructure.persistence.repository.JpaUserSubscriptionRepository;
import com.hoaug.movieapi.modules.user.domain.model.AccountStatus;
import com.hoaug.movieapi.modules.user.domain.model.Role;
import com.hoaug.movieapi.modules.user.infrastructure.persistence.repository.JpaUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetDashboardSummaryUseCase {

  private final JpaUserRepository userRepository;
  private final JpaMovieRepository movieRepository;
  private final JpaEpisodeRepository episodeRepository;
  private final JpaCommentRepository commentRepository;
  private final JpaReportRepository reportRepository;
  private final JpaReviewRepository reviewRepository;
  private final JpaUserSubscriptionRepository userSubscriptionRepository;
  private final JpaPaymentTransactionRepository paymentTransactionRepository;
  private final JpaAdvertisementRepository advertisementRepository;
  private final javax.sql.DataSource dataSource;

  @Transactional(readOnly = true)
  public DashboardSummaryResponse execute () {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime dayStart = now.minusDays(1);
    LocalDateTime weekStart = now.minusDays(7);
    LocalDateTime monthStart = now.minusDays(30);

    long totalUsers = userRepository.count();
    long activeUsers = userRepository.countByAccountStatus(AccountStatus.ACTIVE);
    long blockedUsers = userRepository.countByAccountStatus(AccountStatus.BLOCKED);
    long pendingUsers = userRepository.countByAccountStatus(AccountStatus.PENDING);
    long adminUsers = userRepository.countByRole(Role.ROLE_ADMIN);
    long newUsers7d = userRepository.countByCreatedAtAfter(weekStart);
    long onlineUsers24h = userRepository.countByLastLoginAtAfter(dayStart);

    long totalMovies = movieRepository.count();
    long publishedMovies = movieRepository.countByMovieStatus(MovieStatus.PUBLISHED);
    long draftMovies = movieRepository.countByMovieStatus(MovieStatus.DRAFT);
    long archivedMovies = movieRepository.countByMovieStatus(MovieStatus.ARCHIVED);
    long upcomingMovies = movieRepository.countByMovieStatus(MovieStatus.UPCOMING);
    long singleMovies = movieRepository.countByMovieType(MovieType.SINGLE);
    long seriesMovies = movieRepository.countByMovieType(MovieType.SERIES);
    long premiumMovies = movieRepository.countByIsPremiumOnlyTrue();
    long newMovies30d = movieRepository.countByPublishedAtAfter(monthStart);
    long totalEpisodes = episodeRepository.count();
    long totalViews = movieRepository.sumViewCount();
    long totalFavorites = movieRepository.sumFavoriteCount();
    BigDecimal averageRating = movieRepository.averageRatingAcrossCatalog().setScale(2,
        RoundingMode.HALF_UP);

    long totalComments = commentRepository.count();
    long visibleComments = commentRepository.countByStatus(CommentStatus.VISIBLE);
    long hiddenComments = commentRepository.countByStatus(CommentStatus.HIDDEN);
    long deletedComments = commentRepository.countByStatus(CommentStatus.DELETED);
    long rootComments = commentRepository.countByParentCommentIdIsNull();
    long replyComments = commentRepository.countByParentCommentIdIsNotNull();
    long newComments24h = commentRepository.countByCreatedAtAfter(dayStart);
    long commentLikes = commentRepository.sumLikeCount();
    long totalReviews = reviewRepository.count();

    long totalReports = reportRepository.count();
    long pendingReports = reportRepository.countByStatus(ReportStatus.PENDING);
    long resolvedReports = reportRepository.countByStatus(ReportStatus.RESOLVED);
    long rejectedReports = reportRepository.countByStatus(ReportStatus.REJECTED);
    long commentReports = reportRepository.countByCommentIdIsNotNull();
    long reviewReports = reportRepository.countByReviewIdIsNotNull();
    long newReports24h = reportRepository.countByCreatedAtAfter(dayStart);

    long totalSubscriptions = userSubscriptionRepository.count();
    long activeSubscriptions = userSubscriptionRepository
        .findByStatusAndEndAtBetween(SubscriptionStatus.ACTIVE, now, now.plusYears(100)).size();
    long expiringSubscriptions7d = userSubscriptionRepository
        .findByStatusAndEndAtBetween(SubscriptionStatus.ACTIVE, now, now.plusDays(7)).size();
    BigDecimal totalRevenue = paymentTransactionRepository.sumAmountByStatus(PaymentStatus.SUCCESS);
    BigDecimal revenue30d = paymentTransactionRepository
        .sumAmountByStatusAndPaidAtAfter(PaymentStatus.SUCCESS, monthStart);
    long successfulPayments = paymentTransactionRepository.countByStatus(PaymentStatus.SUCCESS);
    long pendingPayments = paymentTransactionRepository.countByStatus(PaymentStatus.PENDING);
    long newPayments7d = paymentTransactionRepository
        .countByStatusAndPaidAtAfter(PaymentStatus.SUCCESS, weekStart);

    List<DashboardSummaryResponse.AdminRankingCard> rankingCards = List.of(
        rankingCard("Top 10 phim bộ xem nhiều nhất", "Ưu tiên theo lượt xem của phim bộ đã xuất bản",
            "violet",
            movieRepository.findTopSeriesMovies(PageRequest.of(0, 10)).stream()
                .map(movie -> rankingItem(movie.getId(), movie.getSlug(), movie.getTitle(),
                    compact(movie.getViewCount()), movie.getReleaseYear() + " • " + movie.getCountry(),
                    compact(movie.getFavoriteCount()) + " yêu thích"))
                .toList()),
        rankingCard("Top 10 phim lẻ xem nhiều nhất", "Phim lẻ đang kéo lưu lượng truy cập", "emerald",
            movieRepository.findTopSingleMovies(PageRequest.of(0, 10)).stream()
                .map(movie -> rankingItem(movie.getId(), movie.getSlug(), movie.getTitle(),
                    compact(movie.getViewCount()), movie.getReleaseYear() + " • " + movie.getCountry(),
                    compact(movie.getFavoriteCount()) + " yêu thích"))
                .toList()),
        rankingCard("Top 10 phim tương tác mạnh nhất", "Tổng hợp lượt xem, yêu thích, review và bình luận",
            "cyan",
            movieRepository.findMostInteractedMovies(PageRequest.of(0, 10)).stream()
                .map(movie -> rankingItem(movie.getId(), movie.getSlug(), movie.getTitle(),
                    compact(movie.getViewCount()), compact(movie.getFavoriteCount()) + " yêu thích",
                    movie.getAverageRating().setScale(1, RoundingMode.HALF_UP) + "★ • "
                        + movie.getTotalReviews() + " đánh giá"))
                .toList()),
        rankingCard("Top gói doanh thu nhiều nhất", "Gói thuê bao xếp theo tổng thanh toán thành công",
            "amber",
            userSubscriptionRepository.findTopPlanRevenue(PageRequest.of(0, 10)).stream()
                .map(plan -> rankingItem(null, null, plan.getPlanName(), money(plan.getRevenue()),
                    plan.getPlanCode(), plan.getSubscriptions() + " lượt đăng ký"))
                .toList()));

    List<DashboardSummaryResponse.AdminMetric> heroMetrics = List.of(
        metric("Doanh thu", money(totalRevenue), "+" + money(revenue30d), "emerald",
            "Doanh thu / 30 ngày"),
        metric("Thanh toán", successfulPayments, signed(newPayments7d), "violet",
            pendingPayments + " giao dịch chờ"),
        metric("Người dùng", totalUsers, signed(newUsers7d), "cyan", newUsers7d + " mới / 7 ngày"),
        metric("Lượt xem", totalViews, signed(newMovies30d), "amber",
            newMovies30d + " phim mới / 30 ngày"));

    List<DashboardSummaryResponse.AdminServerPerformance> serverPerformance = List.of(
        DashboardSummaryResponse.AdminServerPerformance.builder().label("API CPU Load (%)")
            .color("#38bdf8").data(generatePerformanceSeries(getSystemCpuLoad())).build(),
        DashboardSummaryResponse.AdminServerPerformance.builder().label("API RAM Usage (%)")
            .color("#8b5cf6").data(generatePerformanceSeries(getJvmRamUsage())).build(),
        DashboardSummaryResponse.AdminServerPerformance.builder().label("DB Connections")
            .color("#f59e0b").data(generatePerformanceSeries(getActiveDbConnections())).build(),
        DashboardSummaryResponse.AdminServerPerformance.builder().label("Request Speed (ms)")
            .color("#10b981")
            .data(generatePerformanceSeries(
                (int) com.hoaug.movieapi.common.filter.HttpLoggingFilter.lastResponseTime))
            .build());

    return DashboardSummaryResponse.builder().serverPerformance(serverPerformance)
        .metrics(heroMetrics)
        .metricGroups(List.of(group("Nội dung", "Tình trạng thư viện phim và tập", List.of(
            metric("Tổng phim", totalMovies, percent(publishedMovies, totalMovies), "violet",
                "Tất cả trạng thái"),
            metric("Đã xuất bản", publishedMovies, percent(publishedMovies, totalMovies), "emerald",
                "Đang hiển thị"),
            metric("Phim nháp", draftMovies, "Chờ", "amber", "Cần biên tập"),
            metric("Sắp chiếu", upcomingMovies, "Sớm", "cyan", "Sắp phát hành"),
            metric("Lưu trữ", archivedMovies, "Đóng", "amber", "Đã lưu trữ"),
            metric("Tập phim", totalEpisodes, "+", "cyan", "Tập trong dữ liệu"))),
            group("Người dùng", "Sức khỏe tài khoản và đăng nhập", List.of(
                metric("Tổng tài khoản", totalUsers, signed(newUsers7d), "cyan", "Tăng 7 ngày"),
                metric("Đang hoạt động", activeUsers, percent(activeUsers, totalUsers), "emerald",
                    "Có thể sử dụng"),
                metric("Online 24h", onlineUsers24h, percent(onlineUsers24h, totalUsers), "emerald",
                    "Đăng nhập gần đây"),
                metric("Chờ kích hoạt", pendingUsers, "Chờ", "amber", "Cần xác minh"),
                metric("Bị khóa", blockedUsers, "Rủi ro", "amber", "Không thể sử dụng"),
                metric("Quản trị viên", adminUsers, "Quyền cao", "violet", "Tài khoản quản trị"),
                metric("Tỷ lệ hoạt động", activeUsers, percent(activeUsers, totalUsers), "emerald",
                    "Tài khoản khỏe"),
                metric("Tăng trưởng 7 ngày", newUsers7d, signed(newUsers7d), "cyan",
                    "Người dùng mới"))),
            group("Tương tác", "Bình luận, đánh giá, yêu thích", List.of(
                metric("Bình luận", totalComments, signed(newComments24h), "emerald",
                    "Mới trong 24h"),
                metric("Hiển thị", visibleComments, percent(visibleComments, totalComments),
                    "emerald", "Đang hiện"),
                metric("Ẩn", hiddenComments, "Cần duyệt", "amber", "Không hiển thị"),
                metric("Đã xoá", deletedComments, "Đã gỡ", "amber", "Đã xoá khỏi luồng"),
                metric("Phản hồi", replyComments, percent(replyComments, totalComments), "cyan",
                    "Trong chuỗi bình luận"),
                metric("Đánh giá", totalReviews, "+", "violet", "Đánh giá phim"),
                metric("Like bình luận", commentLikes, "+", "emerald", "Tổng like bình luận"),
                metric("Yêu thích", totalFavorites, "+", "emerald", "Tổng yêu thích phim"))),
            group("Kiểm duyệt", "Hàng đợi báo cáo và xử lý", List.of(
                metric("Báo cáo", totalReports, signed(newReports24h), "amber", "Mới trong 24h"),
                metric("Chờ xử lý", pendingReports, "Ưu tiên", "amber", "Đang chờ"),
                metric("Đã xử lý", resolvedReports, percent(resolvedReports, totalReports),
                    "emerald", "Hoàn tất"),
                metric("Từ bình luận", commentReports, percent(commentReports, totalReports),
                    "cyan", "Nguồn bình luận"),
                metric("Từ đánh giá", reviewReports, percent(reviewReports, totalReports), "violet",
                    "Nguồn đánh giá"),
                metric("Từ chối", rejectedReports, "Đóng", "amber", "Không chấp nhận"))),
            group("Doanh thu", "Giao dịch, gói thuê bao và nội dung trả phí", List.of(
                metric("Tổng doanh thu", money(totalRevenue), "+" + money(revenue30d), "emerald",
                    "Giao dịch thành công"),
                metric("Doanh thu 30 ngày", money(revenue30d), signed(newPayments7d), "emerald",
                    "Thanh toán gần đây"),
                metric("Thanh toán thành công", successfulPayments,
                    percent(successfulPayments, Math.max(1, successfulPayments + pendingPayments)),
                    "violet", "Đã ghi nhận"),
                metric("Thanh toán chờ", pendingPayments, "Cần theo dõi", "amber", "Chưa hoàn tất"),
                metric("Gói đang hoạt động", activeSubscriptions,
                    percent(activeSubscriptions, totalSubscriptions), "emerald", "Chưa hết hạn"),
                metric("Gói sắp hết hạn", expiringSubscriptions7d, "Theo dõi", "amber",
                    "Cần giữ chân"),
                metric("Phim premium", premiumMovies, percent(premiumMovies, totalMovies), "cyan",
                    "Kho trả phí"),
                metric("Tổng gói", totalSubscriptions, "+", "violet", "Tất cả bản ghi")))))
        .workload(
            List.of(workload("Báo cáo chờ xử lý", pendingReports, "#f59e0b", "Ưu tiên kiểm duyệt"),
                workload("Phim nháp", draftMovies, "#8b5cf6", "Cần biên tập/xuất bản"),
                workload("Bình luận bị ẩn", hiddenComments, "#10b981", "Cần xem lại"),
                workload("Tài khoản chờ kích hoạt", pendingUsers, "#38bdf8", "Cần xác minh"),
                workload("Gói sắp hết hạn", expiringSubscriptions7d, "#fb7185", "Rủi ro rời bỏ")))
        .distributions(List.of(distribution("Phim lẻ", singleMovies, "#22c55e", "nội dung"),
            distribution("Phim bộ", seriesMovies, "#8b5cf6", "nội dung"),
            distribution("Phim premium", premiumMovies, "#06b6d4", "nội dung"),
            distribution("Phim sắp chiếu", upcomingMovies, "#ec4899", "nội dung"),
            distribution("TK đang hoạt động", activeUsers, "#3b82f6", "người dùng"),
            distribution("Gói đang hoạt động", activeSubscriptions, "#eab308", "doanh thu"),
            distribution("Bình luận gốc", rootComments, "#14b8a6", "tương tác"),
            distribution("Phản hồi", replyComments, "#f97316", "tương tác"),
            distribution("Báo cáo bình luận", commentReports, "#f59e0b", "kiểm duyệt"),
            distribution("Báo cáo đánh giá", reviewReports, "#ef4444", "kiểm duyệt")))
        .systemSignals(List.of(
            signal("Doanh thu 30 ngày", money(revenue30d), "success",
                "Giao dịch thành công gần đây"),
            signal("Thanh toán chờ", String.valueOf(pendingPayments),
                pendingPayments > 0 ? "warning" : "success", "Giao dịch chưa hoàn tất"),
            signal("Hàng đợi kiểm duyệt", String.valueOf(pendingReports),
                pendingReports > 0 ? "warning" : "success", "Báo cáo đang chờ"),
            signal("Sức khỏe thư viện", percent(publishedMovies, totalMovies), "success",
                "Tỷ lệ phim đã xuất bản"),
            signal("Hoạt động người dùng", percent(onlineUsers24h, totalUsers), "info",
                "Đăng nhập trong 24h"),
            signal("Nợ nội dung", String.valueOf(draftMovies + hiddenComments),
                draftMovies + hiddenComments > 0 ? "warning" : "success",
                "Phim nháp + bình luận ẩn")))
        .activities(List.of(
            activity("reports", "Báo cáo mới", newReports24h + " báo cáo trong 24 giờ",
                newReports24h > 0 ? "warning" : "success", "24h"),
            activity("comments", "Bình luận mới", newComments24h + " bình luận trong 24 giờ",
                "info", "24h"),
            activity("users", "Người dùng quay lại",
                onlineUsers24h + " người dùng đăng nhập gần đây", "success", "24h"),
            activity("content", "Nội dung mới", newMovies30d + " phim xuất bản trong 30 ngày",
                "info", "30 ngày"),
            activity("subscriptions", "Gói sắp hết hạn",
                expiringSubscriptions7d + " gói trong 7 ngày",
                expiringSubscriptions7d > 0 ? "warning" : "success", "7 ngày")))
        .trendSets(List.of(series(totalUsers, activeUsers, onlineUsers24h, newUsers7d),
            series(totalMovies, publishedMovies, draftMovies, newMovies30d),
            series(totalComments, visibleComments, newComments24h, replyComments),
            series(totalReports, pendingReports, resolvedReports, newReports24h)))
        .mainTrend(series(totalViews, totalFavorites, totalComments, totalReports, totalUsers,
            totalMovies))
        .rankingCards(rankingCards).build();
  }

  private DashboardSummaryResponse.AdminMetricGroup group (String title, String subtitle,
      List<DashboardSummaryResponse.AdminMetric> items) {
    return DashboardSummaryResponse.AdminMetricGroup.builder().title(title).subtitle(subtitle)
        .items(items).build();
  }

  private DashboardSummaryResponse.AdminRankingCard rankingCard (String title, String subtitle,
      String accent, List<DashboardSummaryResponse.AdminRankingItem> items) {
    return DashboardSummaryResponse.AdminRankingCard.builder().title(title).subtitle(subtitle)
        .accent(accent).items(items).build();
  }

  private DashboardSummaryResponse.AdminRankingItem rankingItem (Long id, String slug, String title,
      String value, String detail, String meta) {
    String href = id == null ? null : "/admin/movies?id=" + id;
    return DashboardSummaryResponse.AdminRankingItem.builder().id(id).slug(slug).href(href)
        .title(title).value(value).detail(detail).meta(meta).build();
  }

  private DashboardSummaryResponse.AdminMetric metric (String label, long value, String delta,
      String tone, String helper) {
    return metric(label, compact(value), delta, tone, helper);
  }

  private DashboardSummaryResponse.AdminMetric metric (String label, String value, String delta,
      String tone, String helper) {
    return DashboardSummaryResponse.AdminMetric.builder().label(label).value(value).delta(delta)
        .tone(tone).helper(helper).build();
  }

  private DashboardSummaryResponse.AdminWorkloadItem workload (String name, long value,
      String color, String caption) {
    return DashboardSummaryResponse.AdminWorkloadItem.builder().name(name).value(safeInt(value))
        .color(color).caption(caption).build();
  }

  private DashboardSummaryResponse.AdminDistributionItem distribution (String label, long value,
      String color, String scope) {
    return DashboardSummaryResponse.AdminDistributionItem.builder().label(label)
        .value(safeInt(value)).color(color).scope(scope).build();
  }

  private DashboardSummaryResponse.AdminSystemSignal signal (String label, String value,
      String status, String detail) {
    return DashboardSummaryResponse.AdminSystemSignal.builder().label(label).value(value)
        .status(status).detail(detail).build();
  }

  private DashboardSummaryResponse.AdminActivity activity (String id, String title,
      String description, String severity, String time) {
    return DashboardSummaryResponse.AdminActivity.builder().id(id).title(title)
        .description(description).severity(severity).time(time).build();
  }

  private String compact (long value) {
    if (value >= 1_000_000) {
      return BigDecimal.valueOf(value).divide(BigDecimal.valueOf(1_000_000), 1,
          RoundingMode.HALF_UP) + "M";
    }
    if (value >= 1_000) {
      return BigDecimal.valueOf(value).divide(BigDecimal.valueOf(1_000), 1, RoundingMode.HALF_UP)
          + "K";
    }
    return String.valueOf(value);
  }

  private String signed (long value) {
    return "+" + compact(value);
  }

  private String percent (long value, long total) {
    if (total <= 0)
      return "0%";
    return BigDecimal.valueOf(value * 100.0 / total).setScale(1, RoundingMode.HALF_UP) + "%";
  }

  private int safeInt (long value) {
    return value > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) value;
  }

  private String money (BigDecimal value) {
    if (value == null)
      return "0₫";
    BigDecimal normalized = value.setScale(0, RoundingMode.HALF_UP);
    if (normalized.compareTo(BigDecimal.valueOf(1_000_000)) >= 0) {
      return normalized.divide(BigDecimal.valueOf(1_000_000), 1, RoundingMode.HALF_UP)
          .stripTrailingZeros().toPlainString() + " triệu ₫";
    }
    if (normalized.compareTo(BigDecimal.valueOf(1_000)) >= 0) {
      return normalized.divide(BigDecimal.valueOf(1_000), 1, RoundingMode.HALF_UP)
          .stripTrailingZeros().toPlainString() + " nghìn ₫";
    }
    return normalized.toPlainString() + "₫";
  }

  private String translateAdType (AdvertisementEntity advertisement) {
    if (advertisement.getAdType() == null)
      return "Quảng cáo";
    return switch (advertisement.getAdType().name()) {
    case "BANNER" -> "Banner";
    case "VIDEO" -> "Video";
    case "POPUP" -> "Cửa sổ nổi";
    default -> "Quảng cáo";
    };
  }

  private List<Integer> series (long... values) {
    long max = 1;
    for (long value : values) {
      max = Math.max(max, value);
    }
    final long scale = max;
    return java.util.Arrays.stream(values)
        .mapToInt(value -> (int) Math.max(8, Math.round(value * 100.0 / scale))).boxed().toList();
  }

  private int getSystemCpuLoad () {
    try {
      java.lang.management.OperatingSystemMXBean osBean = java.lang.management.ManagementFactory
          .getOperatingSystemMXBean();
      if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
        double load = ((com.sun.management.OperatingSystemMXBean) osBean).getCpuLoad();
        if (load >= 0.0) {
          return (int) (load * 100);
        }
      }
    } catch (Exception ignored) {
      return 0;
    }
    return 0;
  }

  private int getJvmRamUsage () {
    long totalMemory = Runtime.getRuntime().totalMemory();
    long freeMemory = Runtime.getRuntime().freeMemory();
    if (totalMemory > 0) {
      return (int) (((double) (totalMemory - freeMemory) / totalMemory) * 100);
    }
    return 0;
  }

  private int getActiveDbConnections () {
    try {
      if (dataSource instanceof com.zaxxer.hikari.HikariDataSource) {
        return ((com.zaxxer.hikari.HikariDataSource) dataSource).getHikariPoolMXBean()
            .getActiveConnections();
      }
    } catch (Exception ignored) {
      return 0;
    }
    return 0;
  }

  private List<Integer> generatePerformanceSeries (int currentVal) {
    int safeValue = Math.max(0, Math.min(100, currentVal));
    return java.util.Collections.nCopies(12, safeValue);
  }
}
