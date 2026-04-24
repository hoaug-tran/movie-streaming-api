package com.hoaug.movieapi.modules.recommendation.application.dto.response;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationListResponse implements Serializable {
    private List<MovieRecommendationResponse> items;
}
