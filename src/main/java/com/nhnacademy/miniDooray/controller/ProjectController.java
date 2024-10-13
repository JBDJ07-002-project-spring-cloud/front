package com.nhnacademy.miniDooray.controller;

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
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final RestTemplate restTemplate;

    @Value("${task-api.url}")
    private String taskApiUrl;

    @GetMapping
    public String listProjects(Model model) {
        String url = taskApiUrl + "/project";

        try {
            ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
            List projects = response.getBody();
            model.addAttribute("projects", projects);
        } catch (Exception e) {
            model.addAttribute("error", "프로젝트 목록을 불러오는 중 오류가 발생했습니다.");
        }
        return "project/list";
    }

    @PostMapping
    public String createProject(@RequestParam String projectName, Model model) {
        String url = taskApiUrl + "/project";

        Map<String, String> request = new HashMap<>();
        request.put("projectName", projectName);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return "redirect:/project";
            } else if (response.getStatusCode() == HttpStatus.CONFLICT) {
                model.addAttribute("error", "프로젝트 이름이 중복됩니다.");
                return "project/create";
            } else {
                model.addAttribute("error", "프로젝트 생성에 실패했습니다.");
                return "project/create";
            }
        } catch (Exception e) {
            model.addAttribute("error", "프로젝트 생성 중 오류가 발생했습니다.");
            return "project/create";
        }
    }

    @PatchMapping("/{projectId}")
    public String updateProjectStatus(
            @PathVariable Long projectId,
            @RequestParam String projectStatus,
            Model model
    ) {
        String url = taskApiUrl + "/project/" + projectId;

        Map<String, String> request = new HashMap<>();
        request.put("projectStatus", projectStatus);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.PATCH, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return "redirect:/project";
            } else {
                model.addAttribute("error", "프로젝트 상태 변경에 실패했습니다.");
                return "project/detail";
            }
        } catch (Exception e) {
            model.addAttribute("error", "프로젝트 상태 변경 중 오류가 발생했습니다.");
            return "project/detail";
        }
    }

    @PostMapping("/{projectId}/invite")
    public String inviteProjectMembers(
            @PathVariable Long projectId,
            @RequestParam List<Long> userIds,
            Model model
    ) {
        String url = taskApiUrl + "/project/" + projectId + "/invite";

        Map<String, Object> request = new HashMap<>();
        request.put("userIds", userIds);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return "redirect:/project/" + projectId;
            } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                model.addAttribute("error", "존재하지 않는 사용자가 있습니다.");
                return "project/invite";
            } else {
                model.addAttribute("error", "프로젝트 멤버 초대에 실패했습니다.");
                return "project/invite";
            }
        } catch (Exception e) {
            model.addAttribute("error", "프로젝트 멤버 초대 중 오류가 발생했습니다.");
            return "project/invite";
        }
    }

    // 프로젝트 상세 조회
    @GetMapping("/{projectId}")
    public String getProjectDetail(@PathVariable Long projectId, Model model) {
        String url = taskApiUrl + "/project/" + projectId;

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                Map projectDetail = response.getBody();
                model.addAttribute("project", projectDetail);
                return "project/detail";
            } else {
                model.addAttribute("error", "프로젝트를 불러오는 중 오류가 발생했습니다.");
                return "project/list";
            }
        } catch (Exception e) {
            model.addAttribute("error", "프로젝트를 불러오는 중 오류가 발생했습니다.");
            return "project/list";
        }
    }

    // 프로젝트 태그 등록
    @PostMapping("/{projectId}/tag")
    public String createProjectTag(
            @PathVariable Long projectId,
            @RequestParam String tagName,
            Model model
    ) {
        String url = taskApiUrl + "/project/" + projectId + "/tag";

        Map<String, String> request = new HashMap<>();
        request.put("tagName", tagName);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return "redirect:/project/" + projectId;
            } else {
                model.addAttribute("error", "태그 등록에 실패했습니다.");
                return "project/detail";
            }
        } catch (Exception e) {
            model.addAttribute("error", "태그 등록 중 오류가 발생했습니다.");
            return "project/detail";
        }
    }

    // 프로젝트 태그 수정
    @PutMapping("/{projectId}/tag/{tagId}")
    public String updateProjectTag(
            @PathVariable Long projectId,
            @PathVariable Long tagId,
            @RequestParam String tagName,
            Model model
    ) {
        String url = taskApiUrl + "/project/" + projectId + "/tag/" + tagId;

        Map<String, String> request = new HashMap<>();
        request.put("tagName", tagName);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return "redirect:/project/" + projectId;
            } else {
                model.addAttribute("error", "태그 수정에 실패했습니다.");
                return "project/detail";
            }
        } catch (Exception e) {
            model.addAttribute("error", "태그 수정 중 오류가 발생했습니다.");
            return "project/detail";
        }
    }

    // 프로젝트 태그 삭제
    @DeleteMapping("/{projectId}/tag/{tagId}")
    public String deleteProjectTag(
            @PathVariable Long projectId,
            @PathVariable Long tagId,
            Model model
    ) {
        String url = taskApiUrl + "/project/" + projectId + "/tag/" + tagId;

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return "redirect:/project/" + projectId;
            } else {
                model.addAttribute("error", "태그 삭제에 실패했습니다.");
                return "project/detail";
            }
        } catch (Exception e) {
            model.addAttribute("error", "태그 삭제 중 오류가 발생했습니다.");
            return "project/detail";
        }
    }

    // 프로젝트 마일스톤 등록
    @PostMapping("/{projectId}/milestone")
    public String createProjectMilestone(
            @PathVariable Long projectId,
            @RequestParam String milestoneContent,
            Model model
    ) {
        String url = taskApiUrl + "/project/" + projectId + "/milestone";

        Map<String, String> request = new HashMap<>();
        request.put("milestoneContent", milestoneContent);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return "redirect:/project/" + projectId;
            } else if (response.getStatusCode() == HttpStatus.CONFLICT) {
                model.addAttribute("error", "같은 마일스톤이 이미 존재합니다.");
                return "project/detail";
            } else {
                model.addAttribute("error", "마일스톤 등록에 실패했습니다.");
                return "project/detail";
            }
        } catch (Exception e) {
            model.addAttribute("error", "마일스톤 등록 중 오류가 발생했습니다.");
            return "project/detail";
        }
    }

    // 프로젝트 마일스톤 수정
    @PutMapping("/{projectId}/milestone/{milestoneId}")
    public String updateProjectMilestone(
            @PathVariable Long projectId,
            @PathVariable Long milestoneId,
            @RequestParam String milestoneContent,
            Model model
    ) {
        String url = taskApiUrl + "/project/" + projectId + "/milestone/" + milestoneId;

        Map<String, String> request = new HashMap<>();
        request.put("milestoneContent", milestoneContent);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return "redirect:/project/" + projectId;
            } else {
                model.addAttribute("error", "마일스톤 수정에 실패했습니다.");
                return "project/detail";
            }
        } catch (Exception e) {
            model.addAttribute("error", "마일스톤 수정 중 오류가 발생했습니다.");
            return "project/detail";
        }
    }

    // 프로젝트 마일스톤 삭제
    @DeleteMapping("/{projectId}/milestone/{milestoneId}")
    public String deleteProjectMilestone(
            @PathVariable Long projectId,
            @PathVariable Long milestoneId,
            Model model
    ) {
        String url = taskApiUrl + "/project/" + projectId + "/milestone/" + milestoneId;

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return "redirect:/project/" + projectId;
            } else {
                model.addAttribute("error", "마일스톤 삭제에 실패했습니다.");
                return "project/detail";
            }
        } catch (Exception e) {
            model.addAttribute("error", "마일스톤 삭제 중 오류가 발생했습니다.");
            return "project/detail";
        }
    }

    // 프로젝트에 속한 멤버 조회
    @GetMapping("/{projectId}/members")
    public String getProjectMembers(@PathVariable Long projectId, Model model) {
        String url = taskApiUrl + "/project/" + projectId + "/members";

        try {
            ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                List members = response.getBody();
                model.addAttribute("members", members);
                return "project/members";
            } else {
                model.addAttribute("error", "프로젝트 멤버를 불러오는 중 오류가 발생했습니다.");
                return "project/detail";
            }
        } catch (Exception e) {
            model.addAttribute("error", "프로젝트 멤버를 불러오는 중 오류가 발생했습니다.");
            return "project/detail";
        }
    }
}
