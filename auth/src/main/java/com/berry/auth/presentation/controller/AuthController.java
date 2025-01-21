package com.berry.auth.presentation.controller;

import com.berry.auth.application.service.AuthService;
import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/logout")
  public ApiResponse<Void> logout(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
      @CookieValue(value = "refreshToken", required = false) String refreshToken,
      HttpServletResponse response) {

    authService.logout(authorizationHeader, refreshToken, response);

    return ApiResponse.OK(ResSuccessCode.SUCCESS, "로그아웃이 완료되었습니다.");
  }
}
