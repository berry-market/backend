package com.berry.user.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateEmailRequest(
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "이메일 주소 형식이 잘못되었습니다.")
    String email
) {
}
