package com.nhnacademy.miniDooray.server.dto.tag;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TagUpdateRequest {
    @NotBlank(message = "태그 이름은 필수 입력입니다.")
    private String tagName;
}
