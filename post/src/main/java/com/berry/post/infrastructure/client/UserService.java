package com.berry.post.infrastructure.client;

import com.berry.common.response.ApiResponse;
import com.berry.post.application.dto.GetInternalUserResponse;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserClient userClient;

  @Async
  public CompletableFuture<ApiResponse<GetInternalUserResponse>> getInternalUserByIdAsync(Long userId) {
    return CompletableFuture.completedFuture(userClient.getInternalUserById(userId));
  }

}
