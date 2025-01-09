package com.berry.auth.infrastructure.client;

import com.berry.auth.infrastructure.security.dto.UserInfoResDto;
import com.berry.auth.infrastructure.security.dto.UserLoginResDto;
import com.berry.common.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserClient {

  @GetMapping("/server/v1/users/by-id")
  ApiResponse<UserInfoResDto> getUserById(@RequestParam Long userId);

  @GetMapping("/server/v1/users/by-nickname")
  ApiResponse<UserLoginResDto> getUserByNickname(@RequestParam String nickname);
}

