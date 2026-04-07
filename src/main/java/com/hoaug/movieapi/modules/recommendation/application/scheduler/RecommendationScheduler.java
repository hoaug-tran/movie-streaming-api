package com.hoaug.movieapi.modules.recommendation.application.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RecommendationScheduler {

  @Scheduled(fixedRate = 604800000)
  public void generateWeeklyRecommendations () {
  }
}
