package com.berry.gateway.filter;

import com.berry.gateway.exception.GatewayException;
import com.berry.gateway.redis.RedisTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.List;
import java.util.Objects;
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

  private static final List<Route> EXCLUDED_PATHS = List.of(
      new Route("POST", "/api/v1/auth/login"),
      new Route("POST", "/api/v1/users/sign-up"),
      new Route("GET", "/api/v1/posts/**"),
      new Route("GET", "/api/v1/categories/**"),
      new Route("GET", "/api/v1/reviews/**"),
      new Route("GET", "/api/v1/users/check-id/**")
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
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();
        String accessToken = null;

        if (path.startsWith("/ws/")) {
          accessToken = extractWebSocketToken(exchange);
        } else {
          String authHeader = exchange.getRequest().getHeaders()
              .getFirst(HttpHeaders.AUTHORIZATION);
          accessToken = authHeader != null && authHeader.startsWith("Bearer ")
              ? authHeader.substring(7)
              : null;
        }

        if (EXCLUDED_PATHS.stream().anyMatch(route -> route.matches(method, path))
            && accessToken == null) {
          return chain.filter(exchange);
        }

        if (accessToken != null && redisTokenRepository.isBlacklisted(accessToken)) {
          throw new GatewayException(HttpStatus.UNAUTHORIZED, "Access token is blacklisted");
        }

        if (!isValidAccessToken(accessToken)) {

          String refreshToken = getCookieValue(exchange);

          if (refreshToken == null || !redisTokenRepository.isRefreshTokenValid(refreshToken)) {
            throw new GatewayException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
          }
          return handleRefreshToken(exchange, chain, refreshToken);
        }

        return addHeaders(exchange, chain, accessToken);
      } catch (GatewayException ex) {
        throw ex;
      } catch (Exception ex) {
        throw new GatewayException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
      }
    };
  }

  private String extractWebSocketToken(ServerWebExchange exchange) {
    List<String> protocols = exchange.getRequest().getHeaders().get("Sec-WebSocket-Protocol");
    if (protocols != null && !protocols.isEmpty()) {
      String token = protocols.get(0);
      if (token.startsWith("Bearer ")) {
        return token.substring(7);
      }
    }
    return null;
  }


  private Mono<Void> handleRefreshToken(ServerWebExchange exchange, GatewayFilterChain chain,
      String refreshToken) {
    return webClientBuilder.build()
        .post()
        .uri("lb://auth-service/server/v1/auth/refresh")
        .header("Refresh-Token", refreshToken)
        .retrieve()
        .bodyToMono(String.class)
        .flatMap(newAccessToken -> {

          exchange.getResponse().getHeaders().add("New-Access-Token", newAccessToken);

          return addHeaders(exchange, chain, newAccessToken);
        })
        .onErrorResume(ex -> Mono.error(
            new GatewayException(HttpStatus.UNAUTHORIZED, "Failed to refresh access token")));
  }

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

  private boolean isValidAccessToken(String accessToken) {
    if (accessToken == null) {
      return false;
    }
    try {
      Claims claims = extractClaims(accessToken);

      String tokenType = claims.get("type", String.class);
      if (!"access".equals(tokenType)) {
        throw new GatewayException(HttpStatus.UNAUTHORIZED, "Not an access token");
      }
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  private Claims extractClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private String getCookieValue(ServerWebExchange exchange) {
    return exchange.getRequest().getCookies().getFirst("refreshToken") != null
        ? Objects.requireNonNull(exchange.getRequest().getCookies().getFirst("refreshToken"))
        .getValue()
        : null;
  }

  private record Route(String method, String pathPattern) {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    public boolean matches(String method, String path) {
      return this.method.equalsIgnoreCase(method) && PATH_MATCHER.match(this.pathPattern, path);
    }
  }

  public static class Config {

  }
}