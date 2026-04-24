package com.hoaug.movieapi.modules.watchhistory.application.dto.response;

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
public class WatchHistoryListResponse implements Serializable {
    private List<WatchHistoryResponse> items;
}
