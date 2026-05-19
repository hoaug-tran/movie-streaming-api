package com.hoaug.movieapi.modules.chatbot.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.MovieListResponse;
import com.hoaug.movieapi.modules.movie.application.dto.response.MovieSummaryResponse;
import com.hoaug.movieapi.modules.movie.application.usecase.GetTrendingMoviesUseCase;
import com.hoaug.movieapi.modules.recommendation.application.dto.response.RecommendationListResponse;
import com.hoaug.movieapi.modules.recommendation.application.usecase.GetMyRecommendationsUseCase;
import com.hoaug.movieapi.modules.watchhistory.application.dto.response.ContinueWatchingResponse;
import com.hoaug.movieapi.modules.watchhistory.application.usecase.GetContinueWatchingUseCase;

@Component
public class ChatContextBuilder {

  private static final Logger log = LoggerFactory.getLogger(ChatContextBuilder.class);
  private static final int TRENDING_LIMIT = 6;
  private static final int HISTORY_LIMIT = 4;
  private static final int RECOMMEND_LIMIT = 4;

  private final GetTrendingMoviesUseCase getTrendingMoviesUseCase;
  private final GetContinueWatchingUseCase getContinueWatchingUseCase;
  private final GetMyRecommendationsUseCase getMyRecommendationsUseCase;

  public ChatContextBuilder(GetTrendingMoviesUseCase getTrendingMoviesUseCase,
      GetContinueWatchingUseCase getContinueWatchingUseCase,
      GetMyRecommendationsUseCase getMyRecommendationsUseCase) {
    this.getTrendingMoviesUseCase = getTrendingMoviesUseCase;
    this.getContinueWatchingUseCase = getContinueWatchingUseCase;
    this.getMyRecommendationsUseCase = getMyRecommendationsUseCase;
  }

  public String buildContextBlock(ChatUserContext userContext) {
    StringBuilder builder = new StringBuilder();

    String trending = formatTrending();
    if (!trending.isEmpty()) {
      builder.append("\n\nDữ liệu trang (cập nhật mỗi lần hỏi):\n").append(trending);
    }

    if (userContext != null && userContext.authenticated() && userContext.userId() != null) {
      String history = formatHistory(userContext.userId());
      if (!history.isEmpty()) {
        builder.append("\n\nLịch sử xem gần đây của người dùng:\n").append(history);
      }

      String recommendations = formatRecommendations(userContext.userId());
      if (!recommendations.isEmpty()) {
        builder.append("\n\nGợi ý cá nhân hoá hiện có:\n").append(recommendations);
      }
    }

    if (builder.length() == 0) {
      return "";
    }

    builder.append(
        "\n\nKhi gợi ý phim, hãy ưu tiên dùng các phim trên. Nếu user hỏi phim không có trong danh sách, hãy gợi ý họ vào /discovery hoặc /movies để khám phá thêm.");
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
    Integer year = movie.getReleaseYear();
    String slug = Optional.ofNullable(movie.getSlug()).orElse("");
    String type = "TV_SERIES".equalsIgnoreCase(movie.getMovieType()) ? "phim bộ" : "phim lẻ";
    return "- %s%s (%s) — /movies/%s".formatted(title,
        year != null ? " (" + year + ")" : "", type, slug);
  }

  private String formatHistory(Long userId) {
    try {
      List<ContinueWatchingResponse> items = getContinueWatchingUseCase.execute(userId);
      if (items == null || items.isEmpty()) {
        return "";
      }
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
    return "- %s%s — đang xem dở, /watch/%s".formatted(title,
        episode != null ? " (tập " + episode + ")" : "", slug);
  }

  private String formatRecommendations(Long userId) {
    try {
      RecommendationListResponse response = getMyRecommendationsUseCase.execute(userId);
      if (response == null || response.getItems() == null || response.getItems().isEmpty()) {
        return "";
      }
      return response.getItems().stream()
          .limit(RECOMMEND_LIMIT)
          .filter(rec -> rec.getMovie() != null)
          .map(rec -> "- %s — /movies/%s".formatted(
              Optional.ofNullable(rec.getMovie().getTitle()).orElse("?"),
              Optional.ofNullable(rec.getMovie().getSlug()).orElse("")))
          .collect(Collectors.joining("\n"));
    } catch (Exception ex) {
      log.warn("Cannot fetch recommendations for chatbot context", ex);
      return "";
    }
  }
}
