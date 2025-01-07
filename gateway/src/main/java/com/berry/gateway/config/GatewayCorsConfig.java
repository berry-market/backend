package com.berry.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class GatewayCorsConfig {

  @Bean
  public CorsWebFilter corsWebFilter() {
    CorsConfiguration config = new CorsConfiguration();
    config.addAllowedOrigin("http://localhost:3000"); // 요청을 허용할 프론트엔드 도메인
    config.addAllowedHeader("*"); // 모든 헤더 허용
    config.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
    config.setAllowCredentials(true); // 인증 정보 포함 허용
    config.addExposedHeader("Authorization"); // 클라이언트에서 접근 가능한 헤더
    config.addExposedHeader("Set-Cookie");

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config); // 모든 경로에 대해 CORS 설정 적용

    return new CorsWebFilter(source);
  }
}