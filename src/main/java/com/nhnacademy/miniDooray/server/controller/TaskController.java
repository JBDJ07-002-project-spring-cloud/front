package com.nhnacademy.miniDooray.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/task")
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
            @RequestParam String taskName,
            @RequestParam(required = false) String taskTags,
            @RequestParam(required = false) String milestone,
            @RequestParam(required = false) String content,
            Model model
    ) {
        String url = taskApiUrl + "/task/" + projectId;

        Map<String, Object> request = new HashMap<>();
        request.put("taskName", taskName);
        request.put("taskTags", taskTags);
        request.put("milestone", milestone);
        request.put("content", content);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return "redirect:/task/" + projectId;
            } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                model.addAttribute("error", "존재하지 않는 프로젝트입니다.");
                return "task/create";
            } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                model.addAttribute("error", "유효하지 않은 입력입니다.");
                return "task/create";
            } else {
                model.addAttribute("error", "태스크 생성에 실패했습니다.");
                return "task/create";
            }
        } catch (Exception e) {
            model.addAttribute("error", "태스크 생성 중 오류가 발생했습니다.");
            return "task/create";
        }
    }

    @PutMapping("/{projectId}/{taskId}")
    public String updateTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @RequestParam String taskName,
            @RequestParam(required = false) String taskTags,
            @RequestParam(required = false) String milestone,
            @RequestParam(required = false) String content,
            Model model
    ) {
        String url = taskApiUrl + "/task/" + projectId + "/" + taskId;

        Map<String, Object> request = new HashMap<>();
        request.put("taskName", taskName);
        request.put("taskTags", taskTags);
        request.put("milestone", milestone);
        request.put("content", content);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return "redirect:/task/" + projectId + "/" + taskId;
            } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                model.addAttribute("error", "존재하지 않는 태스크입니다.");
                return "task/edit";
            } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                model.addAttribute("error", "유효하지 않은 입력입니다.");
                return "task/edit";
            } else {
                model.addAttribute("error", "태스크 수정에 실패했습니다.");
                return "task/edit";
            }
        } catch (Exception e) {
            model.addAttribute("error", "태스크 수정 중 오류가 발생했습니다.");
            return "task/edit";
        }
    }

    @DeleteMapping("/{projectId}/{taskId}")
    public String deleteTask(@PathVariable Long projectId, @PathVariable Long taskId, Model model) {
        String url = taskApiUrl + "/task/" + projectId + "/" + taskId;

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return "redirect:/task/" + projectId;
            } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                model.addAttribute("error", "존재하지 않는 태스크입니다.");
                return "task/list";
            } else {
                model.addAttribute("error", "태스크 삭제에 실패했습니다.");
                return "task/list";
            }
        } catch (Exception e) {
            model.addAttribute("error", "태스크 삭제 중 오류가 발생했습니다.");
            return "task/list";
        }
    }

    // 태스크 코멘트 등록
    @PostMapping("/{projectId}/{taskId}/comment")
    public String createTaskComment(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @RequestParam String content,
            Model model
    ) {
        String url = taskApiUrl + "/task/" + projectId + "/" + taskId + "/comment";

        Map<String, String> request = new HashMap<>();
        request.put("content", content);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return "redirect:/task/" + projectId + "/" + taskId;
            } else {
                model.addAttribute("error", "코멘트 등록에 실패했습니다.");
                return "task/detail";
            }
        } catch (Exception e) {
            model.addAttribute("error", "코멘트 등록 중 오류가 발생했습니다.");
            return "task/detail";
        }
    }

    // 태스크 코멘트 수정
    @PutMapping("/{projectId}/{taskId}/{commentId}")
    public String updateTaskComment(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @RequestParam String content,
            Model model
    ) {
        String url = taskApiUrl + "/task/" + projectId + "/" + taskId + "/" + commentId;

        Map<String, String> request = new HashMap<>();
        request.put("content", content);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return "redirect:/task/" + projectId + "/" + taskId;
            } else {
                model.addAttribute("error", "코멘트 수정에 실패했습니다.");
                return "task/detail";
            }
        } catch (Exception e) {
            model.addAttribute("error", "코멘트 수정 중 오류가 발생했습니다.");
            return "task/detail";
        }
    }

    // 태스크 코멘트 삭제
    @DeleteMapping("/{projectId}/{taskId}/{commentId}")
    public String deleteTaskComment(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            Model model
    ) {
        String url = taskApiUrl + "/task/" + projectId + "/" + taskId + "/" + commentId;

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return "redirect:/task/" + projectId + "/" + taskId;
            } else {
                model.addAttribute("error", "코멘트 삭제에 실패했습니다.");
                return "task/detail";
            }
        } catch (Exception e) {
            model.addAttribute("error", "코멘트 삭제 중 오류가 발생했습니다.");
            return "task/detail";
        }
    }
}
