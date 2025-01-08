package com.berry.user.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
    @NotBlank(message = "이메일이 누락되었습니다.")
    @Email(message = "이메일 주소 형식이 잘못되었습니다.")
    String email,

    @Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])(?!.*\\s).+$",
        message = "비밀번호는 영어, 숫자, 특수문자를 반드시 포함해야 하며 공백은 사용할 수 없습니다."
    )
    @Size(min = 8, max = 50, message = "비밀번호는 최소 8글자 이상 최대 50글자 이하로 작성해야 합니다.")
    String password,

    @NotBlank(message = "아이디가 누락되었습니다.")
    @Size(min = 5, max = 20, message = "아이디는 최소 5글자 이상 최대 20글자 이하로 작성해야 합니다.")
    String nickname
) {
}
