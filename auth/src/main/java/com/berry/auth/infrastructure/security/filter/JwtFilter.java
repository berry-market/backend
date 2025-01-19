package com.berry.auth.infrastructure.security.filter;

import com.berry.auth.infrastructure.repository.RedisTokenRepository;
import com.berry.auth.infrastructure.security.config.FilterConfig;
import com.berry.auth.infrastructure.security.jwt.JwtAuthenticationToken;
import com.berry.auth.infrastructure.security.jwt.JwtTokenValidator;
import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ApiResponse;
import com.berry.common.response.ResErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtTokenValidator jwtTokenValidator;
  private final RedisTokenRepository redisTokenRepository;

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    return FilterConfig.EXCLUDED_PATHS.contains(path);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain)
      throws IOException, ServletException {

      String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      handleException(response, new CustomApiException(ResErrorCode.UNAUTHORIZED, "Need Authorization"));
      return;
    }

    try {
      String accessToken = authHeader.substring(7);

      if (redisTokenRepository.isBlacklisted(accessToken)) {
        throw new CustomApiException(ResErrorCode.UNAUTHORIZED, "Access token is blacklisted");
      }

      jwtTokenValidator.validateAccessToken(accessToken);

      Claims claims = jwtTokenValidator.extractClaims(accessToken);
      setAuthentication(request, claims);

      filterChain.doFilter(request, response);

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

  private void setAuthentication(HttpServletRequest request, Claims claims) {
    JwtAuthenticationToken authentication = new JwtAuthenticationToken(
        Long.parseLong(claims.getSubject()),
        claims.get("nickname", String.class),
        claims.get("role", String.class),
        null
    );
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    authentication.setAuthenticated(true);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

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