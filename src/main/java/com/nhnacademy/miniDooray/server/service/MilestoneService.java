package com.nhnacademy.miniDooray.server.service;

import com.nhnacademy.miniDooray.server.adapter.Adapter;
import com.nhnacademy.miniDooray.server.dto.account.ResponseDto;
import com.nhnacademy.miniDooray.server.dto.milestone.MilestoneRequestDto;
import com.nhnacademy.miniDooray.server.exception.milestone.MilestoneException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@RequiredArgsConstructor
public class MilestoneService {

    private final Adapter adapter;

    @Value("${task-api.url}")
    private String taskApiUrl;

    public ResponseDto registerMilestone(Long projectId, MilestoneRequestDto requestDto, String userId) {
        String url = taskApiUrl + "/project/" + projectId + "/milestone";

        try {
            ResponseEntity<ResponseDto> response = adapter.postWithHeader(url, requestDto, ResponseDto.class, "X-USER-ID", userId);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            throw new MilestoneException("마일스톤 생성 실패");
        } catch (HttpClientErrorException e) {
            throw new MilestoneException("마일스톤 생성 실패");
        }
    }

    public ResponseDto updateMilestone(Long projectId, Long milestoneId, MilestoneRequestDto requestDto, String userId) {
        String url = taskApiUrl + "/project/" + projectId + "/milestone/" + milestoneId;

        try {
            ResponseEntity<ResponseDto> response = adapter.putWithHeader(url, requestDto, ResponseDto.class, "X-USER-ID", userId);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            throw new MilestoneException("마일스톤 수정 실패");
        } catch (HttpClientErrorException e) {
            throw new MilestoneException("마일스톤 수정 실패");
        }
    }

    public ResponseDto deleteMilestone(Long projectId, Long milestoneId, String userId) {
        String url = taskApiUrl + "/project/" + projectId + "/milestone/" + milestoneId;

        try {
            ResponseEntity<ResponseDto> response = adapter.deleteWithHeader(url, ResponseDto.class, "X-USER-ID", userId);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            throw new MilestoneException("마일스톤 삭제 실패");
        } catch (HttpClientErrorException e) {
            throw new MilestoneException("마일스톤 삭제 실패");
        }
    }
}
