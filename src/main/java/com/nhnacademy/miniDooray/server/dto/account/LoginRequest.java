package com.nhnacademy.miniDooray.server.dto.account;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "사용자 이름은 필수 입력입니다.")
    private String userName;

    @NotBlank(message = "비밀번호는 필수 입력입니다.")
    private String userPassword;
}
