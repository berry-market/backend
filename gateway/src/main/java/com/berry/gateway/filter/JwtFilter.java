package com.berry.gateway.filter;

import com.berry.gateway.exception.GatewayException;
import com.berry.gateway.redis.RedisTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtFilter extends AbstractGatewayFilterFactory<JwtFilter.Config> {

  @Value("${jwt.secret}")
  private String secretKey;

  private final WebClient.Builder webClientBuilder;
  private final RedisTokenRepository redisTokenRepository;

  // 필터 제외 경로
  private static final List<Route> EXCLUDED_PATHS = List.of(
      new Route("POST", "/api/v1/auth/login"),
      new Route("POST", "/api/v1/users/signup"),
      new Route("GET", "/api/v1/posts/**"),
      new Route("GET", "/api/v1/categories/**"),
      new Route("GET", "/api/v1/reviews/**")
  );

  public JwtFilter(WebClient.Builder webClientBuilder, RedisTokenRepository redisTokenRepository) {
    super(Config.class);
    this.webClientBuilder = webClientBuilder;
    this.redisTokenRepository = redisTokenRepository;
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      try {
        // 요청 경로와 메서드 추출
        String path = exchange.getRequest().getPath().toString();
        String method = exchange.getRequest().getMethod().name();

        // Authorization 헤더 추출
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String accessToken = authHeader != null && authHeader.startsWith("Bearer ")
            ? authHeader.substring(7)
            : null;

        // 검증 제외 경로 처리 : "accessToken == null" 일때 필터 통과
        if (EXCLUDED_PATHS.stream().anyMatch(route -> route.matches(method, path))
            && accessToken == null) {
          return chain.filter(exchange);
        }

        // 블랙리스트 확인
        if (accessToken != null && redisTokenRepository.isBlacklisted(accessToken)) {
          throw new GatewayException(HttpStatus.UNAUTHORIZED, "Access token is blacklisted");
        }

        // 액세스 토큰 검증
        if (!isValidAccessToken(accessToken)) {
          // 액세스 토큰이 유효하지 않을 경우 쿠키에서 리프레시 토큰 추출
          String refreshToken = getCookieValue(exchange);

          // 리프레시 토큰 유효성 검증
          if (refreshToken == null || !redisTokenRepository.isRefreshTokenValid(refreshToken)) {
            throw new GatewayException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
          }
          // 액세스 토큰 재발급
          return handleRefreshToken(exchange, chain, refreshToken);
        }

        // 유저정보 및 액세스 토큰 헤더에 추가 및 체인 진행
        return addHeaders(exchange, chain, accessToken);
      } catch (GatewayException ex) {
        throw ex;
      } catch (Exception ex) {
        throw new GatewayException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
      }
    };
  }

  // 리프레시 토큰으로 액세스 토큰 재발급 요청 처리
  private Mono<Void> handleRefreshToken(ServerWebExchange exchange, GatewayFilterChain chain,
      String refreshToken) {
    return webClientBuilder.build()
        .post()
        .uri("lb://auth-service/server/v1/auth/refresh")
        .header("Refresh-Token", refreshToken)
        .retrieve()
        .bodyToMono(String.class)
        .flatMap(newAccessToken -> {
          // 응답 헤더에 새 액세스 토큰 추가
          exchange.getResponse().getHeaders().add("New-Access-Token", newAccessToken);

          return addHeaders(exchange, chain, newAccessToken);
        })
        .onErrorResume(ex -> Mono.error(
            new GatewayException(HttpStatus.UNAUTHORIZED, "Failed to refresh access token")));
  }

  // 요청 헤더에 유저정보 및 액세스토큰 추가
  private Mono<Void> addHeaders(ServerWebExchange exchange, GatewayFilterChain chain,
      String accessToken) {
    Claims claims = extractClaims(accessToken);
    ServerWebExchange mutatedExchange = exchange.mutate()
        .request(exchange.getRequest().mutate()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .header("X-UserId", claims.getSubject())
            .header("X-Nickname", claims.get("nickname", String.class))
            .header("X-Role", claims.get("role", String.class))
            .build())
        .build();

    return chain.filter(mutatedExchange);
  }

  // 액세스 토큰 유효성 검증
  private boolean isValidAccessToken(String accessToken) {
    if (accessToken == null) {
      return false;
    }
    try {
      Claims claims = extractClaims(accessToken);

      // type 필드 존재 여부와 값 확인
      String tokenType = claims.get("type", String.class);
      if (!"access".equals(tokenType)) {
        throw new GatewayException(HttpStatus.UNAUTHORIZED, "Not an access token");
      }
      return true;
    } catch (io.jsonwebtoken.ExpiredJwtException ex) {
      return false; // 만료된 토큰
    } catch (Exception ex) {
      return false;
    }
  }

  // JWT에서 Claims 추출
  private Claims extractClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  // Signing Key 가져오기
  private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  // 쿠키에서 값 추출
  private String getCookieValue(ServerWebExchange exchange) {
    return exchange.getRequest().getCookies().getFirst("refreshToken") != null
        ? exchange.getRequest().getCookies().getFirst("refreshToken").getValue()
        : null;
  }

  // route 설정
  private record Route(String method, String pathPattern) {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    public boolean matches(String method, String path) {
      return this.method.equalsIgnoreCase(method) && PATH_MATCHER.match(this.pathPattern, path);
    }
  }

  public static class Config {

  }
}