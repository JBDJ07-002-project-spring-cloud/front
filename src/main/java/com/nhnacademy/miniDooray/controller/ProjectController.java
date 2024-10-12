package com.nhnacademy.miniDooray.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import org.springframework.security.core.Authentication;

@Controller
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final RestTemplate restTemplate;
    private final String taskApiUrl = "http://localhost:8082";

    @GetMapping
    public String listProjects(Model model, Authentication authentication) {
        String userId = authentication.getName();
        String url = taskApiUrl + "/project/" + userId;

        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        List projects = response.getBody();

        model.addAttribute("projects", projects);
        return "project/list";
    }

    @GetMapping("/{projectId}")
    public String projectDetail(@PathVariable Long projectId, Model model) {
        String url = taskApiUrl + "/project/" + projectId;

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map project = response.getBody();

        model.addAttribute("project", project);
        return "project/detail";
    }

    @PostMapping
    public String createProject(@RequestParam String projectName, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        String url = taskApiUrl + "/project";

        Map<String, String> request = new HashMap<>();
        request.put("projectName", projectName);
        request.put("userId", userId);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return "redirect:/projects";
        } else {
            return "redirect:/projects?error";
        }
    }
}
