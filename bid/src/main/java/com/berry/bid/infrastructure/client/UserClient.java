package com.berry.bid.infrastructure.client;

import com.berry.bid.infrastructure.model.dto.PostInternalView;
import com.berry.bid.infrastructure.model.dto.UserInternalView;
import com.berry.common.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/server/v1/users/by-id")
    ApiResponse<UserInternalView.Response> getUserById(@RequestParam("userId") Long userId);

}
