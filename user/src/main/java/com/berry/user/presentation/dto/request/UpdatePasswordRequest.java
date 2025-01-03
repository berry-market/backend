package com.berry.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdatePasswordRequest(
    @NotBlank(message = "기존 비밀번호는 필수 입력 항목입니다.")
    String currentPassword,
    @Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])(?!.*\\s).+$",
        message = "비밀번호는 영어, 숫자, 특수문자를 반드시 포함해야 하며 공백은 사용할 수 없습니다."
    )
    @Size(min = 8, max = 50, message = "비밀번호는 최소 8글자 이상 최대 50글자 이하로 작성해야 합니다.")
    String newPassword
) {
}
