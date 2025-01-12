package com.berry.auth.presentation.controller;

import com.berry.auth.application.dto.TokenValidResDto;
import com.berry.auth.application.service.AuthService;
import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/server/v1/auth")
@RequiredArgsConstructor
public class InternalController {

  private final AuthService authService;

  @PostMapping("/refresh")
  public ResponseEntity<String> refreshToken(
      @RequestHeader("Refresh-Token") String refreshToken) {
    String newAccessToken = authService.refreshAccessToken(refreshToken);
    return ResponseEntity.ok(newAccessToken);
  }

  @PostMapping("/validate-token")
  public ApiResponse<TokenValidResDto> validateToken(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
    String accessToken = authorizationHeader.substring(7);
    TokenValidResDto userInfo = authService.validateToken(accessToken);
    return ApiResponse.OK(ResSuccessCode.SUCCESS, userInfo);
  }
}
