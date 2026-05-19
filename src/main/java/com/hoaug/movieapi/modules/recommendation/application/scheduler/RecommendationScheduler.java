package com.hoaug.movieapi.modules.recommendation.application.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.recommendation.application.dto.request.GenerateRecommendationsRequest;
import com.hoaug.movieapi.modules.recommendation.application.usecase.GenerateRecommendationsUseCase;
import com.hoaug.movieapi.modules.watchhistory.infrastructure.persistence.repository.JpaWatchHistoryRepository;

@Component
public class RecommendationScheduler {

  private static final Logger log = LoggerFactory.getLogger(RecommendationScheduler.class);
  private static final int RECENT_ACTIVITY_DAYS = 30;
  private static final int BATCH_LIMIT = 12;

  private final GenerateRecommendationsUseCase generateRecommendationsUseCase;
  private final JpaWatchHistoryRepository jpaWatchHistoryRepository;

  public RecommendationScheduler(GenerateRecommendationsUseCase generateRecommendationsUseCase,
      JpaWatchHistoryRepository jpaWatchHistoryRepository) {
    this.generateRecommendationsUseCase = generateRecommendationsUseCase;
    this.jpaWatchHistoryRepository = jpaWatchHistoryRepository;
  }

  @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Ho_Chi_Minh")
  public void generateDailyRecommendations () {
    LocalDateTime since = LocalDateTime.now().minusDays(RECENT_ACTIVITY_DAYS);
    List<Long> activeUserIds;
    try {
      activeUserIds = jpaWatchHistoryRepository.findDistinctUserIdsWithRecentActivity(since);
    } catch (Exception ex) {
      log.warn("Recommendation scheduler failed to load active users: {}", ex.getMessage());
      return;
    }

    if (activeUserIds.isEmpty()) {
      log.info("Recommendation scheduler skipped: no active users in last {} days",
          RECENT_ACTIVITY_DAYS);
      return;
    }

    int success = 0;
    int failed = 0;
    for (Long userId : activeUserIds) {
      if (userId == null)
        continue;
      try {
        GenerateRecommendationsRequest request = new GenerateRecommendationsRequest();
        request.setUserId(userId);
        request.setLimit(BATCH_LIMIT);
        generateRecommendationsUseCase.execute(request);
        success++;
      } catch (Exception ex) {
        failed++;
        log.warn("Failed to generate recommendations for userId={}: {}", userId, ex.getMessage());
      }
    }
    log.info("Recommendation scheduler completed: success={}, failed={}, totalUsers={}", success,
        failed, activeUserIds.size());
  }
}
