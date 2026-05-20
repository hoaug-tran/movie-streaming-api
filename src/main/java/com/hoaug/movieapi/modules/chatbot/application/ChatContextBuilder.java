package com.hoaug.movieapi.modules.chatbot.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.favorite.application.dto.response.FavoriteListResponse;
import com.hoaug.movieapi.modules.favorite.application.dto.response.FavoriteResponse;
import com.hoaug.movieapi.modules.favorite.application.usecase.GetMyFavoritesUseCase;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieListResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieSummaryResponse;
import com.hoaug.movieapi.modules.movie.application.usecase.GetTrendingMoviesUseCase;
import com.hoaug.movieapi.modules.recommendation.application.dto.response.RecommendationListResponse;
import com.hoaug.movieapi.modules.recommendation.application.usecase.GetMyRecommendationsUseCase;
import com.hoaug.movieapi.modules.searchhistory.application.dto.response.SearchHistoryResponse;
import com.hoaug.movieapi.modules.searchhistory.application.usecase.GetMyRecentSearchHistoriesUseCase;
import com.hoaug.movieapi.modules.subscription.application.dto.response.SubscriptionPlanResponse;
import com.hoaug.movieapi.modules.subscription.application.usecase.GetActiveSubscriptionPlansUseCase;
import com.hoaug.movieapi.modules.watchhistory.application.dto.response.ContinueWatchingResponse;
import com.hoaug.movieapi.modules.watchhistory.application.usecase.GetContinueWatchingUseCase;
import com.hoaug.movieapi.modules.watchlist.application.dto.response.WatchlistListResponse;
import com.hoaug.movieapi.modules.watchlist.application.dto.response.WatchlistResponse;
import com.hoaug.movieapi.modules.watchlist.application.usecase.GetMyWatchlistUseCase;

@Component
public class ChatContextBuilder {

  private static final Logger log = LoggerFactory.getLogger(ChatContextBuilder.class);
  private static final int TRENDING_LIMIT = 8;
  private static final int HISTORY_LIMIT = 5;
  private static final int RECOMMEND_LIMIT = 5;
  private static final int FAVORITE_LIMIT = 5;
  private static final int WATCHLIST_LIMIT = 4;
  private static final int SEARCH_LIMIT = 5;

  private final GetTrendingMoviesUseCase getTrendingMoviesUseCase;
  private final GetContinueWatchingUseCase getContinueWatchingUseCase;
  private final GetMyRecommendationsUseCase getMyRecommendationsUseCase;
  private final GetMyFavoritesUseCase getMyFavoritesUseCase;
  private final GetMyWatchlistUseCase getMyWatchlistUseCase;
  private final GetMyRecentSearchHistoriesUseCase getMyRecentSearchHistoriesUseCase;
  private final GetActiveSubscriptionPlansUseCase getActiveSubscriptionPlansUseCase;

  public ChatContextBuilder(
      GetTrendingMoviesUseCase getTrendingMoviesUseCase,
      GetContinueWatchingUseCase getContinueWatchingUseCase,
      GetMyRecommendationsUseCase getMyRecommendationsUseCase,
      GetMyFavoritesUseCase getMyFavoritesUseCase,
      GetMyWatchlistUseCase getMyWatchlistUseCase,
      GetMyRecentSearchHistoriesUseCase getMyRecentSearchHistoriesUseCase,
      GetActiveSubscriptionPlansUseCase getActiveSubscriptionPlansUseCase) {
    this.getTrendingMoviesUseCase = getTrendingMoviesUseCase;
    this.getContinueWatchingUseCase = getContinueWatchingUseCase;
    this.getMyRecommendationsUseCase = getMyRecommendationsUseCase;
    this.getMyFavoritesUseCase = getMyFavoritesUseCase;
    this.getMyWatchlistUseCase = getMyWatchlistUseCase;
    this.getMyRecentSearchHistoriesUseCase = getMyRecentSearchHistoriesUseCase;
    this.getActiveSubscriptionPlansUseCase = getActiveSubscriptionPlansUseCase;
  }

  public String buildContextBlock(ChatUserContext userContext) {
    StringBuilder builder = new StringBuilder();

    String plans = formatPlans();
    if (!plans.isEmpty()) {
      builder.append("\n\n## Các gói đăng ký hiện có (dữ liệu thật từ DB):\n").append(plans);
    }

    String trending = formatTrending();
    if (!trending.isEmpty()) {
      builder.append("\n\n## Phim đang thịnh hành trên Gió Phim:\n").append(trending);
    }

    if (userContext != null && userContext.authenticated() && userContext.userId() != null) {
      Long uid = userContext.userId();

      String history = formatHistory(uid);
      if (!history.isEmpty()) {
        builder.append("\n\n## Người dùng đang xem dở:\n").append(history);
      }

      String recommendations = formatRecommendations(uid);
      if (!recommendations.isEmpty()) {
        builder.append("\n\n## Phim được gợi ý cá nhân hoá cho người dùng:\n").append(recommendations);
      }

      String favorites = formatFavorites(uid);
      if (!favorites.isEmpty()) {
        builder.append("\n\n## Phim yêu thích của người dùng (sở thích):\n").append(favorites);
      }

      String watchlist = formatWatchlist(uid);
      if (!watchlist.isEmpty()) {
        builder.append("\n\n## Danh sách xem sau của người dùng:\n").append(watchlist);
      }

      String searches = formatSearchHistory(uid);
      if (!searches.isEmpty()) {
        builder.append("\n\n## Từ khoá tìm kiếm gần đây của người dùng: ").append(searches);
      }
    }

    if (builder.length() == 0) {
      return "";
    }

    builder.append("""

        ## Hướng dẫn gợi ý phim:
        - Khi đề xuất phim cụ thể, LUÔN dùng định dạng token: [MOVIE:slug:Tên phim] — ví dụ: [MOVIE:squid-game:Trò chơi mực] để frontend render card có thể bấm vào.
        - Ưu tiên gợi ý phim từ các danh sách dữ liệu trên. Nếu không có, hướng dẫn user tìm tại /discovery.
        - Với phim đang xem dở, nhắc user tiếp tục xem.
        - Dựa vào yêu thích và tìm kiếm để suy luận sở thích thể loại của user.""");

    return builder.toString();
  }

  private String formatTrending() {
    try {
      MovieListResponse trending = getTrendingMoviesUseCase.execute(TRENDING_LIMIT);
      if (trending == null || trending.getMovies() == null || trending.getMovies().isEmpty()) {
        return "";
      }
      return trending.getMovies().stream()
          .limit(TRENDING_LIMIT)
          .map(this::formatTrendingItem)
          .collect(Collectors.joining("\n"));
    } catch (Exception ex) {
      log.warn("Cannot fetch trending for chatbot context", ex);
      return "";
    }
  }

  private String formatTrendingItem(MovieSummaryResponse movie) {
    String title = Optional.ofNullable(movie.getTitle()).orElse("?");
    String slug = Optional.ofNullable(movie.getSlug()).orElse("");
    Integer year = movie.getReleaseYear();
    String type = "TV_SERIES".equalsIgnoreCase(movie.getMovieType()) ? "phim bộ" : "phim lẻ";

    String genres = "";
    if (movie.getCategories() != null && !movie.getCategories().isEmpty()) {
      genres = " | " + movie.getCategories().stream()
          .limit(3)
          .map(c -> c.getName())
          .collect(Collectors.joining(", "));
    }

    String rating = "";
    BigDecimal avg = movie.getAverageRating();
    if (avg != null && avg.compareTo(BigDecimal.ZERO) > 0) {
      rating = " | ⭐ " + avg.setScale(1, java.math.RoundingMode.HALF_UP);
    }

    Long views = movie.getViewCount();
    String viewStr = views != null && views > 0 ? " | 👁 " + formatNumber(views) : "";

    return "- [MOVIE:%s:%s] (%s%s)%s%s%s".formatted(
        slug, title,
        type,
        year != null ? " " + year : "",
        genres, rating, viewStr);
  }

  private String formatHistory(Long userId) {
    try {
      List<ContinueWatchingResponse> items = getContinueWatchingUseCase.execute(userId);
      if (items == null || items.isEmpty()) return "";
      return items.stream()
          .limit(HISTORY_LIMIT)
          .filter(item -> item.getMovie() != null)
          .map(this::formatHistoryItem)
          .collect(Collectors.joining("\n"));
    } catch (Exception ex) {
      log.warn("Cannot fetch history for chatbot context", ex);
      return "";
    }
  }

  private String formatHistoryItem(ContinueWatchingResponse item) {
    String title = item.getMovie().getTitle();
    String slug = Optional.ofNullable(item.getMovie().getSlug()).orElse("");
    Integer episode = item.getEpisodeNumber();
    int progress = item.getProgressPercent() != null ? item.getProgressPercent().intValue() : 0;
    return "- [MOVIE:%s:%s]%s — đã xem %d%%".formatted(
        slug, title,
        episode != null ? " tập " + episode : "",
        progress);
  }

  private String formatRecommendations(Long userId) {
    try {
      RecommendationListResponse response = getMyRecommendationsUseCase.execute(userId);
      if (response == null || response.getItems() == null || response.getItems().isEmpty()) return "";
      return response.getItems().stream()
          .limit(RECOMMEND_LIMIT)
          .filter(rec -> rec.getMovie() != null)
          .map(rec -> "- [MOVIE:%s:%s]".formatted(
              Optional.ofNullable(rec.getMovie().getSlug()).orElse(""),
              Optional.ofNullable(rec.getMovie().getTitle()).orElse("?")))
          .collect(Collectors.joining("\n"));
    } catch (Exception ex) {
      log.warn("Cannot fetch recommendations for chatbot context", ex);
      return "";
    }
  }

  private String formatFavorites(Long userId) {
    try {
      FavoriteListResponse response = getMyFavoritesUseCase.execute(userId);
      if (response == null || response.getItems() == null || response.getItems().isEmpty()) return "";
      return response.getItems().stream()
          .limit(FAVORITE_LIMIT)
          .filter(fav -> fav.getMovie() != null)
          .map(fav -> "- [MOVIE:%s:%s]".formatted(
              Optional.ofNullable(fav.getMovie().getSlug()).orElse(""),
              Optional.ofNullable(fav.getMovie().getTitle()).orElse("?")))
          .collect(Collectors.joining("\n"));
    } catch (Exception ex) {
      log.warn("Cannot fetch favorites for chatbot context", ex);
      return "";
    }
  }

  private String formatWatchlist(Long userId) {
    try {
      WatchlistListResponse response = getMyWatchlistUseCase.execute(userId);
      if (response == null || response.getItems() == null || response.getItems().isEmpty()) return "";
      return response.getItems().stream()
          .limit(WATCHLIST_LIMIT)
          .filter(w -> w.getMovie() != null)
          .map(w -> "- [MOVIE:%s:%s]".formatted(
              Optional.ofNullable(w.getMovie().getSlug()).orElse(""),
              Optional.ofNullable(w.getMovie().getTitle()).orElse("?")))
          .collect(Collectors.joining("\n"));
    } catch (Exception ex) {
      log.warn("Cannot fetch watchlist for chatbot context", ex);
      return "";
    }
  }

  private String formatSearchHistory(Long userId) {
    try {
      List<SearchHistoryResponse> items = getMyRecentSearchHistoriesUseCase.execute(userId, SEARCH_LIMIT);
      if (items == null || items.isEmpty()) return "";
      return items.stream()
          .map(SearchHistoryResponse::getKeyword)
          .filter(k -> k != null && !k.isBlank())
          .collect(Collectors.joining(", "));
    } catch (Exception ex) {
      log.warn("Cannot fetch search history for chatbot context", ex);
      return "";
    }
  }

  private String formatPlans() {
    try {
      List<SubscriptionPlanResponse> plans = getActiveSubscriptionPlansUseCase.execute();
      if (plans == null || plans.isEmpty()) return "";
      return plans.stream()
          .map(this::formatPlanItem)
          .collect(Collectors.joining("\n"));
    } catch (Exception ex) {
      log.warn("Cannot fetch subscription plans for chatbot context", ex);
      return "";
    }
  }

  private String formatPlanItem(SubscriptionPlanResponse plan) {
    String name = Optional.ofNullable(plan.getName()).orElse("?");
    String code = Optional.ofNullable(plan.getCode()).orElse("?");
    BigDecimal price = plan.getPrice();
    String priceStr = price != null ? formatVnd(price) : "?";
    Integer days = plan.getDurationDays();
    Integer maxDevices = plan.getMaxDevices();
    String quality = Optional.ofNullable(plan.getVideoQuality()).orElse("?");
    boolean adsFree = Boolean.TRUE.equals(plan.getHasAdsFree());
    boolean offlineDownload = "PREMIUM_PLUS".equalsIgnoreCase(code);
    boolean exclusive = "PREMIUM".equalsIgnoreCase(code) || "PREMIUM_PLUS".equalsIgnoreCase(code);

    return ("- **%s** (mã `%s`): %s / %d ngày | tối đa %d thiết bị | chất lượng %s | %s quảng cáo | %s phim độc quyền | %s tải phim offline")
        .formatted(
            name, code, priceStr,
            days != null ? days : 0,
            maxDevices != null ? maxDevices : 1,
            quality,
            adsFree ? "✅ không" : "❌ có",
            exclusive ? "✅ có" : "❌ không",
            offlineDownload ? "✅ có" : "❌ không");
  }

  private String formatVnd(BigDecimal value) {
    long v = value.longValue();
    if (v >= 1_000_000) return "%.1fM₫".formatted(v / 1_000_000.0);
    if (v >= 1_000) return "%dK₫".formatted(v / 1_000);
    return v + "₫";
  }

  private String formatNumber(long n) {
    if (n >= 1_000_000) return "%.1fM".formatted(n / 1_000_000.0);
    if (n >= 1_000) return "%.1fK".formatted(n / 1_000.0);
    return String.valueOf(n);
  }
}
