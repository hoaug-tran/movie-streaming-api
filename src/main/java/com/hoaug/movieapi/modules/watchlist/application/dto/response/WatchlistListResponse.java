package com.hoaug.movieapi.modules.watchlist.application.dto.response;

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
public class WatchlistListResponse implements Serializable {
    private List<WatchlistResponse> items;
}
