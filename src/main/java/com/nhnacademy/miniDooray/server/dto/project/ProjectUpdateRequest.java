package com.nhnacademy.miniDooray.server.dto.project;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectUpdateRequest {
    @NotNull(message = "프로젝트 상태는 필수 입력입니다.")
    private ProjectStatus projectStatus;
}
