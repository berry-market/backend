package com.berry.auth.presentation.controller;

import com.berry.auth.application.service.AuthService;
import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResErrorCode;
import com.berry.common.response.ResSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class ExternalController {

  private final AuthService authService;

  @PostMapping("/logout")
  public ResponseEntity<ApiResponse<Void>> logout(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
      throw new CustomApiException(ResErrorCode.UNAUTHORIZED,
          "인증정보가 없거나 유효하지 않습니다");
    }

    String accessToken = authorizationHeader.substring(7);
    authService.logout(accessToken);
    return ResponseEntity.ok(ApiResponse.OK(ResSuccessCode.SUCCESS, "로그아웃이 완료되었습니다."));
  }
}
