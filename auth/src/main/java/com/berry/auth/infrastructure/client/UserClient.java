package com.berry.auth.infrastructure.client;

import com.berry.auth.infrastructure.security.dto.UserResDto;
import com.berry.common.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {

  @GetMapping("/server/v1/users/{nickname}")
  ApiResponse<UserResDto> getUserByNickname(@PathVariable("nickname") String nickname);

}

