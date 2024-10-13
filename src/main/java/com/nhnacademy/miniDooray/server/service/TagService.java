package com.nhnacademy.miniDooray.server.service;

import com.nhnacademy.miniDooray.server.adapter.Adapter;
import com.nhnacademy.miniDooray.server.dto.account.ResponseDto;
import com.nhnacademy.miniDooray.server.dto.tag.TagCreateRequest;
import com.nhnacademy.miniDooray.server.dto.tag.TagUpdateRequest;
import com.nhnacademy.miniDooray.server.exception.tag.TagException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@RequiredArgsConstructor
public class TagService {

    private final Adapter adapter;

    @Value("${task-api.url}")
    private String taskApiUrl;

    public ResponseDto createTag(Long projectId, TagCreateRequest tagCreateRequest, String userId) {
        String url = taskApiUrl + "/project/" + projectId + "/tag";

        try {
            ResponseEntity<ResponseDto> response = adapter.postWithHeader(url, tagCreateRequest, ResponseDto.class, "X-USER-ID", userId);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            throw new TagException("태그 생성 실패");
        } catch (HttpClientErrorException e) {
            throw new TagException("태그 생성 실패");
        }
    }

    public ResponseDto updateTag(Long projectId, Long tagId, TagUpdateRequest tagUpdateRequest, String userId) {
        String url = taskApiUrl + "/project/" + projectId + "/tag/" + tagId;

        try {
            ResponseEntity<ResponseDto> response = adapter.putWithHeader(url, tagUpdateRequest, ResponseDto.class, "X-USER-ID", userId);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            throw new TagException("태그 수정 실패");
        } catch (HttpClientErrorException e) {
            throw new TagException("태그 수정 실패");
        }
    }

    public ResponseDto deleteTag(Long projectId, Long tagId, String userId) {
        String url = taskApiUrl + "/project/" + projectId + "/tag/" + tagId;

        try {
            ResponseEntity<ResponseDto> response = adapter.deleteWithHeader(url, ResponseDto.class, "X-USER-ID", userId);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            throw new TagException("태그 삭제 실패");
        } catch (HttpClientErrorException e) {
            throw new TagException("태그 삭제 실패");
        }
    }
}
