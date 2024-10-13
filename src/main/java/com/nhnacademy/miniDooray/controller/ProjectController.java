package com.nhnacademy.miniDooray.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Controller
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final RestTemplate restTemplate;

    @Value("${task-api.url}")
    private String taskApiUrl;

    @GetMapping
    public String listProjects(Model model, Authentication authentication) {
        String userId = authentication.getName();
        String url = taskApiUrl + "/project/" + userId;

        try {
            ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
            List projects = response.getBody();
            model.addAttribute("projects", projects);
        } catch (Exception e) {
            model.addAttribute("error", "프로젝트 목록을 불러오는 중 오류가 발생했습니다.");
        }
        return "project/list";
    }

    @GetMapping("/{projectId}")
    public String projectDetail(@PathVariable Long projectId, Model model) {
        String url = taskApiUrl + "/project/" + projectId;

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            Map project = response.getBody();
            model.addAttribute("project", project);
        } catch (Exception e) {
            model.addAttribute("error", "프로젝트 정보를 불러오는 중 오류가 발생했습니다.");
        }
        return "project/detail";
    }

    @PostMapping
    public String createProject(@RequestParam String projectName, Authentication authentication, Model model) {
        String userId = authentication.getName();
        String url = taskApiUrl + "/project";

        Map<String, String> request = new HashMap<>();
        request.put("projectName", projectName);
        request.put("userId", userId);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return "redirect:/projects";
            } else {
                model.addAttribute("error", "프로젝트 생성에 실패했습니다.");
                return "project/create";
            }
        } catch (Exception e) {
            model.addAttribute("error", "프로젝트 생성 중 오류가 발생했습니다.");
            return "project/create";
        }
    }
}
