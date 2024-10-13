package com.nhnacademy.miniDooray.server.controller;

import com.nhnacademy.miniDooray.server.dto.account.ResponseDto;
import com.nhnacademy.miniDooray.server.dto.project.ProjectCreateRequest;
import com.nhnacademy.miniDooray.server.dto.project.ProjectResponse;
import com.nhnacademy.miniDooray.server.dto.project.ProjectUpdateRequest;
import com.nhnacademy.miniDooray.server.exception.project.ProjectCreationException;
import com.nhnacademy.miniDooray.server.exception.project.ProjectUpdateException;
import com.nhnacademy.miniDooray.server.service.ProjectService;
import com.nhnacademy.miniDooray.server.dto.project.InviteMemberRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public String listProjects(Authentication authentication, Model model) {
        String userId = authentication.getName();

        try {
            List<ProjectResponse> projects = projectService.getProjects(userId);
            model.addAttribute("projects", projects);
        } catch (Exception e) {
            model.addAttribute("error", "프로젝트 목록을 불러오는 중 오류가 발생했습니다.");
        }
        return "project/list";
    }

    @GetMapping("/create")
    public String createProjectForm(ProjectCreateRequest projectCreateRequest) {
        return "project/create";
    }

    @PostMapping("/create")
    public String createProject(@Valid @ModelAttribute ProjectCreateRequest projectCreateRequest,
                                BindingResult bindingResult,
                                Authentication authentication,
                                Model model) {
        if (bindingResult.hasErrors()) {
            return "project/create";
        }

        String userId = authentication.getName();

        try {
            ResponseDto response = projectService.createProject(projectCreateRequest, userId);
            return "redirect:/project";
        } catch (ProjectCreationException e) {
            model.addAttribute("error", e.getMessage());
            return "project/create";
        }
    }

    @GetMapping("/{projectId}")
    public String getProjectDetail(@PathVariable Long projectId, Model model) {
        try {
            ProjectResponse project = projectService.getProjectDetail(projectId);
            model.addAttribute("project", project);
            return "project/detail";
        } catch (Exception e) {
            model.addAttribute("error", "프로젝트를 불러오는 중 오류가 발생했습니다.");
            return "project/list";
        }
    }

    @PostMapping("/{projectId}/update")
    public String updateProjectStatus(@PathVariable Long projectId,
                                      @Valid @ModelAttribute ProjectUpdateRequest projectUpdateRequest,
                                      BindingResult bindingResult,
                                      Authentication authentication,
                                      Model model) {
        if (bindingResult.hasErrors()) {
            return "project/detail";
        }

        String userId = authentication.getName();

        try {
            ResponseDto response = projectService.updateProjectStatus(projectId, projectUpdateRequest, userId);
            return "redirect:/project/" + projectId;
        } catch (ProjectUpdateException e) {
            model.addAttribute("error", e.getMessage());
            return "project/detail";
        }
    }

    @GetMapping("/{projectId}/invite")
    public String inviteProjectMembersForm(@PathVariable Long projectId, InviteMemberRequest inviteMemberRequest, Model model) {
        model.addAttribute("projectId", projectId);
        return "project/invite";
    }

    @PostMapping("/{projectId}/invite")
    public String inviteProjectMembers(@PathVariable Long projectId,
                                       @Valid @ModelAttribute InviteMemberRequest inviteMemberRequest,
                                       BindingResult bindingResult,
                                       Model model) {
        if (bindingResult.hasErrors()) {
            return "project/invite";
        }

        try {
            ResponseDto response = projectService.inviteProjectMembers(projectId, inviteMemberRequest);
            return "redirect:/project/" + projectId;
        } catch (Exception e) {
            model.addAttribute("error", "프로젝트 멤버 초대 중 오류가 발생했습니다.");
            return "project/invite";
        }
    }

}
