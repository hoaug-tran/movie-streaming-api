package com.hoaug.movieapi.modules.user.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.user.domain.repository.UserRepository;

@Component
public class DeleteUserUseCase {

  private final UserRepository userRepository;

  public DeleteUserUseCase(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void execute (Long userId) {
    userRepository.findById(userId).orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));

    userRepository.deleteById(userId);
  }
}
