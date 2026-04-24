package com.hoaug.movieapi.modules.movie.application.dto.response;

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
public class CategoryListResponse implements Serializable {
    private List<CategoryResponse> categories;
}
