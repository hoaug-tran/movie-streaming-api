package com.hoaug.movieapi.modules.dashboard.presentation.rest.admin;

import com.hoaug.movieapi.modules.dashboard.application.dto.response.DashboardSummaryResponse;
import com.hoaug.movieapi.modules.dashboard.application.usecase.GetDashboardSummaryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix:/api/v1}/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final GetDashboardSummaryUseCase getDashboardSummaryUseCase;

    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public DashboardSummaryResponse getSummary() {
        return getDashboardSummaryUseCase.execute();
    }
}
