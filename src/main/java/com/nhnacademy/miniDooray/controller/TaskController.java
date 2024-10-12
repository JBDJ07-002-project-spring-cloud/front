package com.nhnacademy.miniDooray.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
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
    private final String taskApiUrl = "http://localhost:8082";

    @GetMapping("/{projectId}")
    public String listTasks(@PathVariable Long projectId, Model model) {
        String url = taskApiUrl + "/task/" + projectId;

        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        List tasks = response.getBody();

        model.addAttribute("tasks", tasks);
        return "task/list";
    }

    @GetMapping("/{projectId}/{taskId}")
    public String taskDetail(@PathVariable Long projectId, @PathVariable Long taskId, Model model) {
        String url = taskApiUrl + "/task/" + projectId + "/" + taskId;

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map task = response.getBody();

        model.addAttribute("task", task);
        return "task/detail";
    }

    @PostMapping("/{projectId}")
    public String createTask(
        @PathVariable Long projectId,
        @RequestParam Map<String, String> params,
        HttpSession session
    ) {
        String userId = (String) session.getAttribute("userId");
        String url = taskApiUrl + "/task/" + projectId;

        params.put("userId", userId);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, params, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return "redirect:/tasks/" + projectId;
        } else {
            return "redirect:/tasks/" + projectId + "?error";
        }
    }
}
