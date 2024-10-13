package com.nhnacademy.miniDooray.server.controller;


import com.nhnacademy.miniDooray.server.dto.account.ResponseDto;
import com.nhnacademy.miniDooray.server.dto.tag.TagCreateRequest;
import com.nhnacademy.miniDooray.server.dto.tag.TagUpdateRequest;
import com.nhnacademy.miniDooray.server.exception.tag.TagException;
import com.nhnacademy.miniDooray.server.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/project/{projectId}/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/create")
    public String createTagForm(@PathVariable Long projectId, TagCreateRequest tagCreateRequest, Model model) {
        model.addAttribute("projectId", projectId);
        return "tag/create";
    }

    @PostMapping("/create")
    public String createTag(@PathVariable Long projectId,
                            @Valid @ModelAttribute TagCreateRequest tagCreateRequest,
                            BindingResult bindingResult,
                            Authentication authentication,
                            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("projectId", projectId);
            return "tag/create";
        }

        String userId = authentication.getName();

        try {
            ResponseDto response = tagService.createTag(projectId, tagCreateRequest, userId);
            return "redirect:/project/" + projectId;
        } catch (TagException e) {
            model.addAttribute("error", e.getMessage());
            return "tag/create";
        }
    }

    @GetMapping("/{tagId}/edit")
    public String editTagForm(@PathVariable Long projectId,
                              @PathVariable Long tagId,
                              TagUpdateRequest tagUpdateRequest,
                              Model model) {
        model.addAttribute("projectId", projectId);
        model.addAttribute("tagId", tagId);
        // 기존 태그 정보를 가져와서 tagUpdateRequest에 설정할 수 있습니다.
        return "tag/edit";
    }

    @PostMapping("/{tagId}/edit")
    public String updateTag(@PathVariable Long projectId,
                            @PathVariable Long tagId,
                            @Valid @ModelAttribute TagUpdateRequest tagUpdateRequest,
                            BindingResult bindingResult,
                            Authentication authentication,
                            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("projectId", projectId);
            model.addAttribute("tagId", tagId);
            return "tag/edit";
        }

        String userId = authentication.getName();

        try {
            ResponseDto response = tagService.updateTag(projectId, tagId, tagUpdateRequest, userId);
            return "redirect:/project/" + projectId;
        } catch (TagException e) {
            model.addAttribute("error", e.getMessage());
            return "tag/edit";
        }
    }

    @PostMapping("/{tagId}/delete")
    public String deleteTag(@PathVariable Long projectId,
                            @PathVariable Long tagId,
                            Authentication authentication,
                            Model model) {
        String userId = authentication.getName();

        try {
            ResponseDto response = tagService.deleteTag(projectId, tagId, userId);
            return "redirect:/project/" + projectId;
        } catch (TagException e) {
            model.addAttribute("error", e.getMessage());
            return "project/detail";
        }
    }
}
