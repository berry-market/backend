package com.berry.auth.infrastructure.security.filter;

import com.berry.auth.infrastructure.security.config.FilterConfig;
import com.berry.auth.infrastructure.security.jwt.JwtTokenValidator;
import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtTokenValidator jwtTokenValidator;

  // 필터 제외 경로
  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    return FilterConfig.EXCLUDED_PATHS.contains(path);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain)
      throws IOException, ServletException {

      // Authorization 헤더에서 토큰 추출
      String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      handleException(response, new CustomApiException(ResErrorCode.UNAUTHORIZED, "Need Authorization"));
      return;
    }

    try {
      String accessToken = authHeader.substring(7);

      // 액세스 토큰 유효성 검증
      jwtTokenValidator.validateAccessToken(accessToken);

      filterChain.doFilter(request, response); // 다음 필터로 진행

    } catch (ExpiredJwtException e) {
      handleException(response,
          new CustomApiException(ResErrorCode.UNAUTHORIZED, "Expired Token"));
    } catch (CustomApiException e) {
      handleException(response, e);
    } catch (Exception e) {
      handleException(response,
          new CustomApiException(ResErrorCode.INTERNAL_SERVER_ERROR));
    }
  }

  // 예외 처리 로직
  private void handleException(HttpServletResponse response, CustomApiException e)
      throws IOException {
    response.setStatus(e.getErrorCode().getHttpStatusCode());
    response.setContentType("application/json");

    ApiResponse<?> errorResponse = ApiResponse.ERROR(
        e.getErrorCode(),
        e.getMessage()
    );

    response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
  }
}