package com.nhnacademy.miniDooray.server.dto.comment;

import lombok.Data;

@Data
public class CommentResponseDto {
    private Long commentId;
    private String content;
    private String userName;
    private String createdAt;
}
