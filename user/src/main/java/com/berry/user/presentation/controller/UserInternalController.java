package com.berry.user.presentation.controller;

import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.user.domain.service.UserService;
import com.berry.user.presentation.dto.response.GetInternalUserResponse;
import com.berry.user.presentation.dto.response.GetLoginUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/server/v1/users")
public class UserInternalController {

    private final UserService userService;

    @GetMapping("/by-id")
    public ApiResponse<GetInternalUserResponse> getInternalUserById(@RequestParam("userId") Long userId) {
        return ApiResponse.OK(ResSuccessCode.READ, userService.getInternalUserById(userId));
    }

    @GetMapping("/by-nickname")
    public ApiResponse<GetLoginUserResponse> getInternalUserByNickname(
        @RequestParam("nickname") String nickname
    ) {
        return ApiResponse.OK(ResSuccessCode.READ, userService.getInternalUserByNickname(nickname));
    }

}
