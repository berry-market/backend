package com.berry.auth.infrastructure.security.filter;

import com.berry.auth.infrastructure.security.dto.CustomUserDetails;
import com.berry.auth.application.dto.LoginReqDto;
import com.berry.auth.application.dto.LoginResDto;
import com.berry.auth.domain.model.Role;
import com.berry.auth.domain.repository.AuthRepository;
import com.berry.auth.infrastructure.security.jwt.JwtTokenFactory;
import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResErrorCode;
import com.berry.common.response.ResSuccessCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenFactory jwtTokenFactory;
  private final AuthRepository authRepository;
  private final int refreshTokenValidity;

  public LoginFilter(AuthenticationManager authenticationManager,
      JwtTokenFactory jwtTokenFactory,
      AuthRepository authRepository,
      int refreshTokenValidity) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenFactory = jwtTokenFactory;
    this.authRepository = authRepository;
    this.refreshTokenValidity = refreshTokenValidity;
    setFilterProcessesUrl("/api/v1/auth/login");
  }


  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException {

    ObjectMapper objectMapper = new ObjectMapper();

    try {
      LoginReqDto loginReq = objectMapper.readValue(request.getInputStream(), LoginReqDto.class);

      UsernamePasswordAuthenticationToken authenticationToken =
          new UsernamePasswordAuthenticationToken(loginReq.getNickname(), loginReq.getPassword());

      return authenticationManager.authenticate(authenticationToken);
    } catch (IOException e) {
      throw new CustomApiException(ResErrorCode.BAD_REQUEST, e.getMessage());
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException {

    CustomUserDetails user = (CustomUserDetails) authResult.getPrincipal();

    // JWT 생성
    String accessToken = jwtTokenFactory.createAccessToken(user.getId(), user.getUsername(),
        Role.valueOf(user.getAuthorities().iterator().next().getAuthority()));
    String refreshToken = jwtTokenFactory.createRefreshToken(user.getId());

    // Refresh Token 저장
    boolean isSaved = authRepository.saveRefreshToken(user.getId(), refreshToken);
    if (!isSaved) {
      throw new CustomApiException(ResErrorCode.INTERNAL_SERVER_ERROR, "Save refresh token failed");
    }

    // Refresh Token을 HttpOnly 쿠키에 저장
    Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setSecure(true);
    refreshTokenCookie.setPath("/");
    refreshTokenCookie.setMaxAge(refreshTokenValidity);
    response.addCookie(refreshTokenCookie);

    // 응답
    LoginResDto loginResDto = LoginResDto.builder()
        .accessToken(accessToken)
        .userId(user.getId())
        .nickname(user.getUsername())
        .role(user.getAuthorities().iterator().next().getAuthority())
        .build();

    ApiResponse<LoginResDto> apiResponse = ApiResponse.OK(ResSuccessCode.SUCCESS, loginResDto);

    response.setContentType("application/json");
    new ObjectMapper().writeValue(response.getOutputStream(), apiResponse);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException failed) throws IOException {

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");

    ObjectMapper objectMapper = new ObjectMapper();

    if (failed.getCause() instanceof CustomApiException) {
      CustomApiException customException = (CustomApiException) failed.getCause();
      ApiResponse<?> errorResponse = ApiResponse.ERROR(
          customException.getErrorCode(),
          customException.getMessage()
      );

      response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    } else {
      ApiResponse<?> errorResponse = ApiResponse.ERROR(
          ResErrorCode.UNAUTHORIZED,
          "Id or Password is incorrect"
      );

      response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
  }
}
