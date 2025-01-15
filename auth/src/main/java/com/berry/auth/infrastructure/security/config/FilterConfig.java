package com.berry.auth.infrastructure.security.config;

import java.util.List;

public class FilterConfig {

  public static final List<String> EXCLUDED_PATHS = List.of(
      "/api/v1/auth/login",
      "/server/v1/auth/refresh",
      "/actuator/prometheus"
  );
}
