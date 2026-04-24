package com.hoaug.movieapi.modules.subscription.application.dto.response;

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
public class SubscriptionListResponse implements Serializable {
    private List<UserSubscriptionResponse> items;
}
