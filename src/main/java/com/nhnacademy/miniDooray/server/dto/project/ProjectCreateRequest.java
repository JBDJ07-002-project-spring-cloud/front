package com.nhnacademy.miniDooray.server.dto.project;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProjectCreateRequest {
    @NotBlank(message = "프로젝트 이름은 필수 입력입니다.")
    private String projectName;
}
