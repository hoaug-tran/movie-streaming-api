package com.hoaug.movieapi.modules.dashboard.application.usecase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hoaug.movieapi.modules.activitylog.domain.model.ActivityScope;
import com.hoaug.movieapi.modules.activitylog.infrastructure.persistence.entity.ActivityLogEntity;
import com.hoaug.movieapi.modules.activitylog.infrastructure.persistence.repository.JpaActivityLogRepository;
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
  private final JpaActivityLogRepository activityLogRepository;
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
        rankingCard("Top 10 phim bộ xem nhiều nhất",
            "Ưu tiên theo lượt xem của phim bộ đã xuất bản", "violet",
            movieRepository.findTopSeriesMovies(PageRequest.of(0, 10)).stream()
                .map(movie -> rankingItem(movie.getId(), movie.getSlug(), movie.getTitle(),
                    compact(movie.getViewCount()),
                    movie.getReleaseYear() + " • " + movie.getCountry(),
                    compact(movie.getFavoriteCount()) + " yêu thích"))
                .toList()),
        rankingCard("Top 10 phim lẻ xem nhiều nhất", "Phim lẻ đang kéo lưu lượng truy cập",
            "emerald",
            movieRepository.findTopSingleMovies(PageRequest.of(0, 10)).stream()
                .map(movie -> rankingItem(movie.getId(), movie.getSlug(), movie.getTitle(),
                    compact(movie.getViewCount()),
                    movie.getReleaseYear() + " • " + movie.getCountry(),
                    compact(movie.getFavoriteCount()) + " yêu thích"))
                .toList()),
        rankingCard("Top 10 phim tương tác mạnh nhất",
            "Xếp hạng theo bình luận và đánh giá, không tính lượt xem", "cyan",
            movieRepository.findMostInteractedMovies(PageRequest.of(0, 10)).stream().map(movie -> {
              long commentCount = movieRepository.countVisibleCommentsByMovieId(movie.getId());
              long reviewCount = movie.getTotalReviews() == null ? 0 : movie.getTotalReviews();
              return rankingItem(movie.getId(), movie.getSlug(), movie.getTitle(),
                  compact(commentCount + reviewCount), compact(commentCount) + " bình luận",
                  compact(reviewCount) + " đánh giá • "
                      + movie.getAverageRating().setScale(1, RoundingMode.HALF_UP) + "★");
            }).toList()),
        rankingCard("Thể loại xem nhiều nhất",
            "Tổng lượt xem của các phim đã xuất bản trong từng thể loại", "rose",
            movieRepository.findTopCategoriesByMovieViews(PageRequest.of(0, 10)).stream()
                .map(this::categoryRankingItem).toList()),
        rankingCard("Phim được yêu thích nhất",
            "Xếp hạng theo tổng lượt tim/yêu thích của từng phim", "emerald",
            movieRepository.findMostFavoritedMovies(PageRequest.of(0, 10)).stream()
                .map(movie -> rankingItem(movie.getId(), movie.getSlug(), movie.getTitle(),
                    compact(movie.getFavoriteCount()),
                    compact(movie.getViewCount()) + " lượt xem",
                    movie.getReleaseYear() + " • " + movie.getCountry()))
                .toList()),
        rankingCard("Top gói doanh thu nhiều nhất",
            "Gói thuê bao xếp theo tổng thanh toán thành công", "amber",
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

    int cpuLoad = getSystemCpuLoad();
    int ramUsage = getJvmRamUsage();
    int dbConnections = getActiveDbConnections();
    int responseTime = (int) com.hoaug.movieapi.common.filter.HttpLoggingFilter.lastResponseTime;

    List<DashboardSummaryResponse.AdminServerPerformance> serverPerformance = List.of(
        performance("CPU API", "#38bdf8", cpuLoad, "%"),
        performance("RAM JVM", "#8b5cf6", ramUsage, "%"),
        performance("Kết nối DB", "#f59e0b", dbConnections, " kết nối"),
        performance("Phản hồi API", "#10b981", responseTime, "ms"));

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
            signal("Kiểm duyệt chờ", String.valueOf(pendingReports),
                pendingReports > 0 ? "warning" : "success", "Báo cáo cần xử lý"),
            signal("Tỷ lệ xuất bản", percent(publishedMovies, totalMovies), "success",
                "Phim đang hiển thị trong thư viện"),
            signal("Người dùng 24h", percent(onlineUsers24h, totalUsers), "info",
                "Tài khoản đăng nhập gần đây"),
            signal("Việc tồn đọng", String.valueOf(draftMovies + hiddenComments),
                draftMovies + hiddenComments > 0 ? "warning" : "success",
                "Phim nháp + bình luận ẩn"),
            signal("Gói sắp hết hạn", String.valueOf(expiringSubscriptions7d),
                expiringSubscriptions7d > 0 ? "warning" : "success", "Trong 7 ngày tới"),
            signal("API phản hồi", responseTime + "ms", responseTime > 800 ? "warning" : "success",
                "Thời gian phản hồi gần nhất")))
        .activities(activityFeed(ActivityScope.USER))
        .userActivities(activityFeed(ActivityScope.USER))
        .adminActivities(activityFeed(ActivityScope.ADMIN))
        .trendSets(List.of(series(totalUsers, activeUsers, onlineUsers24h, newUsers7d),
            series(totalMovies, publishedMovies, draftMovies, newMovies30d),
            series(totalComments, visibleComments, newComments24h, replyComments),
            series(totalReports, pendingReports, resolvedReports, newReports24h)))
        .mainTrend(getDailyRevenueTrend(monthStart, now))
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

  private DashboardSummaryResponse.AdminRankingItem categoryRankingItem(Object[] row) {
    String name = row[1] == null ? "Không rõ thể loại" : row[1].toString();
    String slug = row[2] == null ? null : row[2].toString();
    long totalViews = numberAt(row, 3);
    long movieCount = numberAt(row, 4);
    return DashboardSummaryResponse.AdminRankingItem.builder().id(null).slug(slug)
        .href(slug == null ? null : "/admin/movies?category=" + slug).title(name)
        .value(compact(totalViews)).detail(movieCount + " phim")
        .meta("Tổng lượt xem theo thể loại").build();
  }

  private long numberAt(Object[] row, int index) {
    Object value = row[index];
    if (value instanceof Number number) {
      return number.longValue();
    }
    return value == null ? 0 : Long.parseLong(value.toString());
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

  private List<DashboardSummaryResponse.AdminActivity> activityFeed (ActivityScope scope) {
    return activityLogRepository.findByScopeOrderByCreatedAtDesc(scope, PageRequest.of(0, 8))
        .stream().map(this::activityFromLog).toList();
  }

  private DashboardSummaryResponse.AdminActivity activityFromLog (ActivityLogEntity log) {
    String title = log.getActorName() == null || log.getActorName().isBlank() ? log.getAction()
        : log.getActorName() + " • " + log.getAction();
    return activity(String.valueOf(log.getId()), title, log.getDescription(),
        log.getSeverity().name().toLowerCase(), relativeTime(log.getCreatedAt()));
  }

  private String relativeTime (LocalDateTime createdAt) {
    if (createdAt == null) {
      return "mới";
    }
    long minutes = java.time.Duration.between(createdAt, LocalDateTime.now()).toMinutes();
    if (minutes < 1) {
      return "vừa xong";
    }
    if (minutes < 60) {
      return minutes + " phút trước";
    }
    long hours = minutes / 60;
    if (hours < 24) {
      return hours + " giờ trước";
    }
    return (hours / 24) + " ngày trước";
  }

  private DashboardSummaryResponse.AdminServerPerformance performance (String label, String color,
      int currentValue, String unit) {
    return DashboardSummaryResponse.AdminServerPerformance.builder().label(label).color(color)
        .data(generatePerformanceSeries(currentValue)).value(currentValue + unit).unit(unit)
        .build();
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

  private List<Integer> getDailyRevenueTrend (LocalDateTime startDate, LocalDateTime endDate) {
    List<Object[]> dailyData = paymentTransactionRepository.getDailyRevenueRange(startDate, endDate);
    java.util.Map<java.time.LocalDate, BigDecimal> revenueByDate = new java.util.HashMap<>();
    
    for (Object[] row : dailyData) {
      java.time.LocalDate date = (java.time.LocalDate) row[0];
      BigDecimal revenue = (BigDecimal) row[1];
      revenueByDate.put(date, revenue);
    }
    
    List<Long> dailyValues = new java.util.ArrayList<>();
    LocalDate current = startDate.toLocalDate();
    LocalDate end = endDate.toLocalDate();
    
    while (!current.isAfter(end)) {
      BigDecimal revenue = revenueByDate.getOrDefault(current, BigDecimal.ZERO);
      dailyValues.add(revenue.longValue());
      current = current.plusDays(1);
    }
    
    return series(dailyValues.stream().mapToLong(Long::longValue).toArray());
  }

}
