package com.berry.post.infrastructure.client;

import com.berry.common.response.ApiResponse;
import com.berry.post.application.dto.GetInternalUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserClient {

  @GetMapping("/server/v1/users/by-id")
  ResponseEntity<ApiResponse<GetInternalUserResponse>> getInternalUserById(@RequestParam("userId") Long userId);
}
