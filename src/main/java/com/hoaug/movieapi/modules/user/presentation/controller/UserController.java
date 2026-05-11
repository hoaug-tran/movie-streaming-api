package com.hoaug.movieapi.modules.user.presentation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hoaug.movieapi.common.response.ResponseUtil;
import com.hoaug.movieapi.modules.user.application.dto.request.AdminUpdateUserRequest;
import com.hoaug.movieapi.modules.user.application.dto.request.ChangePasswordRequest;
import com.hoaug.movieapi.modules.user.application.dto.request.StartEmailChangeRequest;
import com.hoaug.movieapi.modules.user.application.dto.request.UpdateUserProfileRequest;
import com.hoaug.movieapi.modules.user.application.dto.request.UpdateUserRoleRequest;
import com.hoaug.movieapi.modules.user.application.dto.request.UpdateUserStatusRequest;
import com.hoaug.movieapi.modules.user.application.dto.request.VerifyEmailChangeOtpRequest;
import com.hoaug.movieapi.modules.user.application.dto.response.EmailChangeResponse;
import com.hoaug.movieapi.modules.user.application.dto.response.UserDetailResponse;
import com.hoaug.movieapi.modules.user.application.dto.response.UserProfileResponse;
import com.hoaug.movieapi.modules.user.application.dto.response.UserSummaryResponse;
import com.hoaug.movieapi.modules.user.application.usecase.AdminUpdateUserUseCase;
import com.hoaug.movieapi.modules.user.application.usecase.ChangeMyPasswordUseCase;
import com.hoaug.movieapi.modules.user.application.usecase.DeleteUserUseCase;
import com.hoaug.movieapi.modules.user.application.usecase.GetMyProfileUseCase;
import com.hoaug.movieapi.modules.user.application.usecase.GetUserByIdUseCase;
import com.hoaug.movieapi.modules.user.application.usecase.GetUsersUseCase;
import com.hoaug.movieapi.modules.user.application.usecase.StartEmailChangeUseCase;
import com.hoaug.movieapi.modules.user.application.usecase.UpdateMyProfileUseCase;
import com.hoaug.movieapi.modules.user.application.usecase.UpdateUserRoleUseCase;
import com.hoaug.movieapi.modules.user.application.usecase.UpdateUserStatusUseCase;
import com.hoaug.movieapi.modules.user.application.usecase.UploadMyAvatarUseCase;
import com.hoaug.movieapi.modules.user.application.usecase.VerifyCurrentEmailChangeUseCase;
import com.hoaug.movieapi.modules.user.application.usecase.VerifyNewEmailChangeUseCase;

import jakarta.validation.Valid;

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
  private final DeleteUserUseCase deleteUserUseCase;
  private final AdminUpdateUserUseCase adminUpdateUserUseCase;
  private final StartEmailChangeUseCase startEmailChangeUseCase;
  private final VerifyCurrentEmailChangeUseCase verifyCurrentEmailChangeUseCase;
  private final VerifyNewEmailChangeUseCase verifyNewEmailChangeUseCase;
  private final UploadMyAvatarUseCase uploadMyAvatarUseCase;

  public UserController(GetMyProfileUseCase getMyProfileUseCase,
      UpdateMyProfileUseCase updateMyProfileUseCase,
      ChangeMyPasswordUseCase changeMyPasswordUseCase, GetUsersUseCase getUsersUseCase,
      GetUserByIdUseCase getUserByIdUseCase, UpdateUserStatusUseCase updateUserStatusUseCase,
      UpdateUserRoleUseCase updateUserRoleUseCase, DeleteUserUseCase deleteUserUseCase,
      AdminUpdateUserUseCase adminUpdateUserUseCase,
      StartEmailChangeUseCase startEmailChangeUseCase,
      VerifyCurrentEmailChangeUseCase verifyCurrentEmailChangeUseCase,
      VerifyNewEmailChangeUseCase verifyNewEmailChangeUseCase,
      UploadMyAvatarUseCase uploadMyAvatarUseCase) {
    this.getMyProfileUseCase = getMyProfileUseCase;
    this.updateMyProfileUseCase = updateMyProfileUseCase;
    this.changeMyPasswordUseCase = changeMyPasswordUseCase;
    this.getUsersUseCase = getUsersUseCase;
    this.getUserByIdUseCase = getUserByIdUseCase;
    this.updateUserStatusUseCase = updateUserStatusUseCase;
    this.updateUserRoleUseCase = updateUserRoleUseCase;
    this.deleteUserUseCase = deleteUserUseCase;
    this.adminUpdateUserUseCase = adminUpdateUserUseCase;
    this.startEmailChangeUseCase = startEmailChangeUseCase;
    this.verifyCurrentEmailChangeUseCase = verifyCurrentEmailChangeUseCase;
    this.verifyNewEmailChangeUseCase = verifyNewEmailChangeUseCase;
    this.uploadMyAvatarUseCase = uploadMyAvatarUseCase;
  }

  @GetMapping("/me")
  public ResponseEntity<UserProfileResponse> getMyProfile (Authentication authentication) {
    return ResponseUtil.ok(getMyProfileUseCase.execute(authentication.getName()));
  }

  @PutMapping("/me")
  public ResponseEntity<UserProfileResponse> updateMyProfile (Authentication authentication,
      @Valid @RequestBody UpdateUserProfileRequest request) {
    return ResponseUtil.ok(updateMyProfileUseCase.execute(authentication.getName(), request));
  }

  @PatchMapping(value = "/me/avatar", consumes = "multipart/form-data")
  public ResponseEntity<UserProfileResponse> uploadMyAvatar (Authentication authentication,
      @RequestPart("avatar") MultipartFile avatar) {
    return ResponseUtil.ok(uploadMyAvatarUseCase.execute(authentication.getName(), avatar));
  }

  @PatchMapping("/me/password")
  public ResponseEntity<Void> changeMyPassword (Authentication authentication,
      @Valid @RequestBody ChangePasswordRequest request) {
    changeMyPasswordUseCase.execute(authentication.getName(), request);
    return ResponseUtil.noContent();
  }

  @PatchMapping("/me/email-change/start")
  public ResponseEntity<EmailChangeResponse> startEmailChange (Authentication authentication,
      @Valid @RequestBody StartEmailChangeRequest request) {
    return ResponseUtil.ok(startEmailChangeUseCase.execute(authentication.getName(), request));
  }

  @PatchMapping("/me/email-change/verify-current")
  public ResponseEntity<EmailChangeResponse> verifyCurrentEmailChange (
      Authentication authentication, @Valid @RequestBody VerifyEmailChangeOtpRequest request) {
    return ResponseUtil
        .ok(verifyCurrentEmailChangeUseCase.execute(authentication.getName(), request));
  }

  @PatchMapping("/me/email-change/verify-new")
  public ResponseEntity<EmailChangeResponse> verifyNewEmailChange (Authentication authentication,
      @Valid @RequestBody VerifyEmailChangeOtpRequest request) {
    return ResponseUtil.ok(verifyNewEmailChangeUseCase.execute(authentication.getName(), request));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/")
  public ResponseEntity<List<UserSummaryResponse>> getUsers () {
    return ResponseUtil.ok(getUsersUseCase.execute());
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/{id}")
  public ResponseEntity<UserDetailResponse> getUserById (@PathVariable Long id) {
    return ResponseUtil.ok(getUserByIdUseCase.execute(id));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PatchMapping("/{id}/status")
  public ResponseEntity<UserDetailResponse> updateUserStatus (@PathVariable Long id,
      @Valid @RequestBody UpdateUserStatusRequest request) {
    return ResponseUtil.ok(updateUserStatusUseCase.execute(id, request));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PatchMapping("/{id}/role")
  public ResponseEntity<UserDetailResponse> updateUserRole (@PathVariable Long id,
      @Valid @RequestBody UpdateUserRoleRequest request) {
    return ResponseUtil.ok(updateUserRoleUseCase.execute(id, request));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<UserDetailResponse> updateUser (@PathVariable Long id,
      @Valid @RequestBody AdminUpdateUserRequest request) {
    return ResponseUtil.ok(adminUpdateUserUseCase.execute(id, request));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser (@PathVariable Long id) {
    deleteUserUseCase.execute(id);
    return ResponseUtil.noContent();
  }

}
