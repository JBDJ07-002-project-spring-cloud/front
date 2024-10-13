package com.nhnacademy.miniDooray.server.dto.project;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class InviteMemberRequest {
    @NotEmpty(message = "사용자 목록은 필수 입력입니다.")
    private List<Long> userIds;
}
