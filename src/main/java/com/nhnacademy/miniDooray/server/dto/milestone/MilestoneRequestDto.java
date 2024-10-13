package com.nhnacademy.miniDooray.server.dto.milestone;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MilestoneRequestDto {
    @NotBlank(message = "마일스톤 이름은 필수 입력입니다.")
    private String milestoneName;
}
