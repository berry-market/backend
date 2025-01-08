package com.berry.auth.infrastructure.security.config;

import com.berry.auth.domain.repository.AuthRepository;
import com.berry.auth.infrastructure.security.filter.JwtFilter;
import com.berry.auth.infrastructure.security.filter.LoginFilter;
import com.berry.auth.infrastructure.security.jwt.JwtTokenFactory;
import com.berry.auth.infrastructure.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtFilter jwtFilter;
  private final AuthenticationConfiguration authenticationConfiguration;
  private final JwtTokenFactory jwtTokenFactory;
  private final AuthRepository authRepository;
  private final CustomUserDetailsService customUserDetailsService;

  @Value("${jwt.refresh.expiration}")
  private int refreshTokenValidity;

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(customUserDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(FilterConfig.EXCLUDED_PATHS.toArray(new String[0])).permitAll()
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtTokenFactory, authRepository, refreshTokenValidity), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}