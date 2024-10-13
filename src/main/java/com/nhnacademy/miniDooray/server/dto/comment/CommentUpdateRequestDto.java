package com.nhnacademy.miniDooray.server.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentUpdateRequestDto {
    @NotBlank(message = "댓글 내용은 필수 입력입니다.")
    private String content;
}
