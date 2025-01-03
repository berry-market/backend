package com.berry.user.presentation.controller;

import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.user.domain.service.UserService;
import com.berry.user.presentation.dto.response.GetInternalUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/server/v1/users")
public class UserInternalController {

    private final UserService userService;

    @GetMapping("{userId}")
    public ResponseEntity<ApiResponse<GetInternalUserResponse>> getInternalUserById(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.READ, userService.getInternalUserById(userId)));
    }

    @GetMapping("{nickname}")
    public ResponseEntity<ApiResponse<GetInternalUserResponse>> getInternalUserByNickname(
        @PathVariable("nickname") String nickname
    ) {
        return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.READ, userService.getInternalUserByNickname(nickname)));
    }

}
