package com.nhnacademy.miniDooray.server.service;

import com.nhnacademy.miniDooray.server.adapter.Adapter;
import com.nhnacademy.miniDooray.server.dto.project.*;
import com.nhnacademy.miniDooray.server.dto.account.ResponseDto;
import com.nhnacademy.miniDooray.server.exception.project.ProjectCreationException;
import com.nhnacademy.miniDooray.server.exception.project.ProjectUpdateException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final Adapter adapter;

    @Value("${task-api.url}")
    private String taskApiUrl;

    public ResponseDto createProject(ProjectCreateRequest projectCreateRequest, String userId) {
        String url = taskApiUrl + "/project";

        try {
            ResponseEntity<ResponseDto> response = adapter.postWithHeader(url, projectCreateRequest, ResponseDto.class, "X-USER-ID", userId);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            throw new ProjectCreationException("프로젝트 생성 실패");
        } catch (HttpClientErrorException e) {
            throw new ProjectCreationException("프로젝트 생성 실패");
        }
    }

    public List<ProjectResponse> getProjects(String userId) {
        String url = taskApiUrl + "/project";

        try {
            ResponseEntity<List<ProjectResponse>> response = adapter.getListWithHeader(url, new ParameterizedTypeReference<>() {}, "X-USER-ID", userId);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            throw new RuntimeException("프로젝트 목록 조회 실패");
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("프로젝트 목록 조회 실패", e);
        }
    }

    public ProjectResponse getProjectDetail(Long projectId) {
        String url = taskApiUrl + "/project/" + projectId;

        try {
            ResponseEntity<ProjectResponse> response = adapter.get(url, ProjectResponse.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            throw new RuntimeException("프로젝트 상세 조회 실패");
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("프로젝트 상세 조회 실패", e);
        }
    }

    public ResponseDto updateProjectStatus(Long projectId, ProjectUpdateRequest projectUpdateRequest, String userId) {
        String url = taskApiUrl + "/project/" + projectId;

        try {
            ResponseEntity<ResponseDto> response = adapter.patchWithHeader(url, projectUpdateRequest, ResponseDto.class, "X-USER-ID", userId);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            throw new ProjectUpdateException("프로젝트 상태 수정 실패");
        } catch (HttpClientErrorException e) {
            throw new ProjectUpdateException("프로젝트 상태 수정 실패");
        }
    }

    public ResponseDto inviteProjectMembers(Long projectId, InviteMemberRequest inviteMemberRequest) {
        String url = taskApiUrl + "/project/" + projectId + "/invite";

        try {
            ResponseEntity<ResponseDto> response = adapter.post(url, inviteMemberRequest, ResponseDto.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            throw new RuntimeException("프로젝트 멤버 초대 실패");
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("프로젝트 멤버 초대 실패", e);
        }
    }

}
