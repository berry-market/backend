package com.berry.user.presentation.controller;

import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.user.domain.service.UserService;
import com.berry.user.presentation.dto.response.GetInternalUserResponse;
import com.berry.user.presentation.dto.response.GetLoginUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/server/v1/users")
public class UserInternalController {

    private final UserService userService;

    @GetMapping("/by-id")
    public ResponseEntity<ApiResponse<GetInternalUserResponse>> getInternalUserById(@RequestParam("userId") Long userId) {
        return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.READ, userService.getInternalUserById(userId)));
    }

    @GetMapping("/by-nickname")
    public ResponseEntity<ApiResponse<GetLoginUserResponse>> getInternalUserByNickname(
        @RequestParam("nickname") String nickname
    ) {
        return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.READ, userService.getInternalUserByNickname(nickname)));
    }

}
