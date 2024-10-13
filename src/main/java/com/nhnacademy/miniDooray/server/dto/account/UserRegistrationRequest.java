package com.nhnacademy.miniDooray.server.dto.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegistrationRequest {
    @NotBlank(message = "사용자 이름은 필수 입력입니다.")
    private String userName;

    @NotBlank(message = "비밀번호는 필수 입력입니다.")
    private String userPassword;

    @NotBlank(message = "이메일은 필수 입력입니다.")
    @Email(message = "유효한 이메일 주소여야 합니다.")
    private String userEmail;
}
