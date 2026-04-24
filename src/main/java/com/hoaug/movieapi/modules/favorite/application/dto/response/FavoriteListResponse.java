package com.hoaug.movieapi.modules.favorite.application.dto.response;

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
public class FavoriteListResponse implements Serializable {
    private List<FavoriteResponse> items;
}
