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
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final RestTemplate restTemplate;

    @Value("${task-api.url}")
    private String taskApiUrl;

    @GetMapping("/{projectId}")
    public String listTasks(@PathVariable Long projectId, Model model) {
        String url = taskApiUrl + "/task/" + projectId;

        try {
            ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
            List tasks = response.getBody();
            model.addAttribute("tasks", tasks);
        } catch (Exception e) {
            model.addAttribute("error", "태스크 목록을 불러오는 중 오류가 발생했습니다.");
        }
        return "task/list";
    }

    @GetMapping("/{projectId}/{taskId}")
    public String taskDetail(@PathVariable Long projectId, @PathVariable Long taskId, Model model) {
        String url = taskApiUrl + "/task/" + projectId + "/" + taskId;

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            Map task = response.getBody();
            model.addAttribute("task", task);
        } catch (Exception e) {
            model.addAttribute("error", "태스크 정보를 불러오는 중 오류가 발생했습니다.");
        }
        return "task/detail";
    }

    @PostMapping("/{projectId}")
    public String createTask(
            @PathVariable Long projectId,
            @RequestParam Map<String, String> params,
            Authentication authentication,
            Model model
    ) {
        String userId = authentication.getName();
        String url = taskApiUrl + "/task/" + projectId;

        params.put("userId", userId);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, params, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return "redirect:/tasks/" + projectId;
            } else {
                model.addAttribute("error", "태스크 생성에 실패했습니다.");
                return "task/create";
            }
        } catch (Exception e) {
            model.addAttribute("error", "태스크 생성 중 오류가 발생했습니다.");
            return "task/create";
        }
    }
}
