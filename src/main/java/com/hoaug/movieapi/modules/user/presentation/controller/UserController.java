package com.hoaug.movieapi.modules.user.presentation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaug.movieapi.modules.user.application.dto.request.ChangePasswordRequest;
import com.hoaug.movieapi.modules.user.application.dto.request.UpdateUserProfileRequest;
import com.hoaug.movieapi.modules.user.application.dto.request.UpdateUserRoleRequest;
import com.hoaug.movieapi.modules.user.application.dto.request.UpdateUserStatusRequest;
import com.hoaug.movieapi.modules.user.application.dto.response.UserDetailResponse;
import com.hoaug.movieapi.modules.user.application.dto.response.UserProfileResponse;
import com.hoaug.movieapi.modules.user.application.dto.response.UserSummaryResponse;
import com.hoaug.movieapi.modules.user.application.usecase.ChangeMyPasswordUseCase;
import com.hoaug.movieapi.modules.user.application.usecase.GetMyProfileUseCase;
import com.hoaug.movieapi.modules.user.application.usecase.GetUserByIdUseCase;
import com.hoaug.movieapi.modules.user.application.usecase.GetUsersUseCase;
import com.hoaug.movieapi.modules.user.application.usecase.UpdateMyProfileUseCase;
import com.hoaug.movieapi.modules.user.application.usecase.UpdateUserRoleUseCase;
import com.hoaug.movieapi.modules.user.application.usecase.UpdateUserStatusUseCase;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("${api.prefix:/api/v1}/users")
public class UserController {
    private final GetMyProfileUseCase getMyProfileUseCase;
    private final UpdateMyProfileUseCase updateMyProfileUseCase;
    private final ChangeMyPasswordUseCase changeMyPasswordUseCase;
    private final GetUsersUseCase getUsersUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final UpdateUserStatusUseCase updateUserStatusUseCase;
    private final UpdateUserRoleUseCase updateUserRoleUseCase;

    public UserController(GetMyProfileUseCase getMyProfileUseCase, UpdateMyProfileUseCase updateMyProfileUseCase,
            ChangeMyPasswordUseCase changeMyPasswordUseCase, GetUsersUseCase getUsersUseCase,
            GetUserByIdUseCase getUserByIdUseCase, UpdateUserStatusUseCase updateUserStatusUseCase,
            UpdateUserRoleUseCase updateUserRoleUseCase) {
        this.getMyProfileUseCase = getMyProfileUseCase;
        this.updateMyProfileUseCase = updateMyProfileUseCase;
        this.changeMyPasswordUseCase = changeMyPasswordUseCase;
        this.getUsersUseCase = getUsersUseCase;
        this.getUserByIdUseCase = getUserByIdUseCase;
        this.updateUserStatusUseCase = updateUserStatusUseCase;
        this.updateUserRoleUseCase = updateUserRoleUseCase;
    }

    @GetMapping("/me")
    public UserProfileResponse getMyProfile(Authentication authentication) {
        return getMyProfileUseCase.execute(authentication.getName());
    }

    @PutMapping("/me")
    public UserProfileResponse updateMyProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateUserProfileRequest request) {
        return updateMyProfileUseCase.execute(authentication.getName(), request);
    }

    @PatchMapping("/me/password")
    public void changeMyPassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request) {
        changeMyPasswordUseCase.execute(authentication.getName(), request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserSummaryResponse> getUsers() {
        return getUsersUseCase.execute();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public UserDetailResponse getUserById(@PathVariable Long id) {
        return getUserByIdUseCase.execute(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/status")
    public UserDetailResponse updateUserStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserStatusRequest request) {
        return updateUserStatusUseCase.execute(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/role")
    public UserDetailResponse updateUserRole(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRoleRequest request) {
        return updateUserRoleUseCase.execute(id, request);
    }

}
