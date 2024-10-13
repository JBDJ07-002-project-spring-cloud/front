package com.nhnacademy.miniDooray.server.service;

import com.nhnacademy.miniDooray.server.adapter.Adapter;
import com.nhnacademy.miniDooray.server.dto.account.ResponseDto;
import com.nhnacademy.miniDooray.server.dto.comment.CommentCreateRequestDto;
import com.nhnacademy.miniDooray.server.dto.comment.CommentResponseDto;
import com.nhnacademy.miniDooray.server.dto.comment.CommentUpdateRequestDto;
import com.nhnacademy.miniDooray.server.exception.comment.CommentException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final Adapter adapter;

    @Value("${task-api.url}")
    private String taskApiUrl;

    public ResponseDto createComment(Long projectId, Long taskId, CommentCreateRequestDto requestDto, String userId) {
        String url = taskApiUrl + "/task/" + projectId + "/" + taskId + "/comment";

        try {
            ResponseEntity<ResponseDto> response = adapter.postWithHeader(url, requestDto, ResponseDto.class, "X-USER-ID", userId);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            throw new CommentException("댓글 생성 실패");
        } catch (HttpClientErrorException e) {
            throw new CommentException("댓글 생성 실패");
        }
    }

    public ResponseDto updateComment(Long projectId, Long taskId, Long commentId, CommentUpdateRequestDto requestDto, String userId) {
        String url = taskApiUrl + "/task/" + projectId + "/" + taskId + "/" + commentId;

        try {
            ResponseEntity<ResponseDto> response = adapter.patchWithHeader(url, requestDto, ResponseDto.class, "X-USER-ID", userId);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            throw new CommentException("댓글 수정 실패");
        } catch (HttpClientErrorException e) {
            throw new CommentException("댓글 수정 실패");
        }
    }

    public ResponseDto deleteComment(Long projectId, Long taskId, Long commentId, String userId) {
        String url = taskApiUrl + "/task/" + projectId + "/" + taskId + "/" + commentId;

        try {
            ResponseEntity<ResponseDto> response = adapter.deleteWithHeader(url, ResponseDto.class, "X-USER-ID", userId);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            throw new CommentException("댓글 삭제 실패");
        } catch (HttpClientErrorException e) {
            throw new CommentException("댓글 삭제 실패");
        }
    }

    public List<CommentResponseDto> getComments(Long projectId, Long taskId) {
        String url = taskApiUrl + "/task/" + projectId + "/" + taskId + "/comments";

        try {
            ResponseEntity<List<CommentResponseDto>> response = adapter.getList(url, new ParameterizedTypeReference<>() {});

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            throw new CommentException("댓글 목록 조회 실패");
        } catch (HttpClientErrorException e) {
            throw new CommentException("댓글 목록 조회 실패");
        }
    }
}
