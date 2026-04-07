package com.hoaug.movieapi.modules.devicesession.application.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.event.UserLoggedInEvent;
import com.hoaug.movieapi.modules.devicesession.application.dto.request.CreateDeviceSessionRequest;
import com.hoaug.movieapi.modules.devicesession.application.usecase.CreateDeviceSessionUseCase;

@Component
public class UserLoginEventListener {
  private final CreateDeviceSessionUseCase createDeviceSessionUseCase;

  public UserLoginEventListener(CreateDeviceSessionUseCase createDeviceSessionUseCase) {
    this.createDeviceSessionUseCase = createDeviceSessionUseCase;
  }

  @EventListener
  public void onUserLoggedIn (UserLoggedInEvent event) {
    CreateDeviceSessionRequest req = new CreateDeviceSessionRequest();
    req.setDeviceName(event.getDeviceName());
    req.setIpAddress(event.getIpAddress());
    createDeviceSessionUseCase.execute(event.getUserId(), req);
  }
}
