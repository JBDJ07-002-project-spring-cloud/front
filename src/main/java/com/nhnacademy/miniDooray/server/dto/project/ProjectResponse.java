package com.nhnacademy.miniDooray.server.dto.project;

import lombok.Data;

@Data
public class ProjectResponse {
    private Long projectId;
    private String projectName;
    private ProjectStatus projectStatus;
}
