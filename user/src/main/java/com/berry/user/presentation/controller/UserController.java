package com.berry.user.presentation.controller;

import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import com.berry.user.domain.service.UserService;
import com.berry.user.presentation.dto.request.SignUpRequest;
import com.berry.user.presentation.dto.response.GetUserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<Void>> signUp(@Valid @RequestBody SignUpRequest request) {
        userService.signUp(request);
        return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.CREATED, "회원가입이 완료되었습니다."));
    }

    @GetMapping("{userId}")
    public ResponseEntity<ApiResponse<GetUserResponse>> getUserById(
        @RequestHeader("X-User-Id") Long headerUserId,
        @RequestHeader("X-User-Role") String role,
        @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.READ, userService.getUserById(headerUserId, userId, role)));
    }

}
