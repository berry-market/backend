package com.berry.common.auditor;


import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

  private static final ThreadLocal<String> manualAuditor = new ThreadLocal<>();

  @Override
  public Optional<String> getCurrentAuditor() {
    // 수동 설정된 Auditor가 있으면 사용
    String manual = manualAuditor.get();
    if (manual != null) {
      return Optional.of(manual);
    }

    // HTTP 요청에서 X-Nickname 헤더 추출
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attributes != null) {
      HttpServletRequest request = attributes.getRequest();
      String nickname = request.getHeader("X-Nickname");
      if (nickname != null && !nickname.isEmpty()) {
        return Optional.of(nickname);
      }
    }

    // 인증된 사용자가 없으면 "system" 반환
    return Optional.of("system");
  }

  // 수동으로 Auditor 설정
  public static void setManualAuditor(String nickname) {
    manualAuditor.set(nickname);
  }

  // Auditor 초기화
  public static void clearManualAuditor() {
    manualAuditor.remove();
  }
}

