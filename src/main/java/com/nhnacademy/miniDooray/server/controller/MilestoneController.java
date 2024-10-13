package com.nhnacademy.miniDooray.server.controller;

import com.nhnacademy.miniDooray.server.dto.account.ResponseDto;
import com.nhnacademy.miniDooray.server.dto.milestone.MilestoneRequestDto;
import com.nhnacademy.miniDooray.server.exception.milestone.MilestoneException;
import com.nhnacademy.miniDooray.server.service.MilestoneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/project")
@RequiredArgsConstructor
public class MilestoneController {

    private final MilestoneService milestoneService;

    @GetMapping("/{projectId}/milestone/create")
    public String createMilestoneForm(@PathVariable Long projectId, MilestoneRequestDto milestoneRequestDto, Model model) {
        model.addAttribute("projectId", projectId);
        return "milestone/create";
    }

    @PostMapping("/{projectId}/milestone/create")
    public String registerMilestone(@PathVariable Long projectId,
                                    @Valid @ModelAttribute MilestoneRequestDto requestDto,
                                    BindingResult bindingResult,
                                    Authentication authentication,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("projectId", projectId);
            return "milestone/create";
        }

        String userId = authentication.getName();

        try {
            ResponseDto response = milestoneService.registerMilestone(projectId, requestDto, userId);
            return "redirect:/project/" + projectId;
        } catch (MilestoneException e) {
            model.addAttribute("error", e.getMessage());
            return "milestone/create";
        }
    }

    @GetMapping("/{projectId}/milestone/{milestoneId}/edit")
    public String editMilestoneForm(@PathVariable Long projectId,
                                    @PathVariable Long milestoneId,
                                    MilestoneRequestDto milestoneRequestDto,
                                    Model model) {
        model.addAttribute("projectId", projectId);
        model.addAttribute("milestoneId", milestoneId);

        return "milestone/edit";
    }

    @PostMapping("/{projectId}/milestone/{milestoneId}/edit")
    public String updateMilestone(@PathVariable Long projectId,
                                  @PathVariable Long milestoneId,
                                  @Valid @ModelAttribute MilestoneRequestDto requestDto,
                                  BindingResult bindingResult,
                                  Authentication authentication,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("projectId", projectId);
            model.addAttribute("milestoneId", milestoneId);
            return "milestone/edit";
        }

        String userId = authentication.getName();

        try {
            ResponseDto response = milestoneService.updateMilestone(projectId, milestoneId, requestDto, userId);
            return "redirect:/project/" + projectId;
        } catch (MilestoneException e) {
            model.addAttribute("error", e.getMessage());
            return "milestone/edit";
        }
    }

    @PostMapping("/{projectId}/milestone/{milestoneId}/delete")
    public String deleteMilestone(@PathVariable Long projectId,
                                  @PathVariable Long milestoneId,
                                  Authentication authentication,
                                  Model model) {
        String userId = authentication.getName();

        try {
            ResponseDto response = milestoneService.deleteMilestone(projectId, milestoneId, userId);
            return "redirect:/project/" + projectId;
        } catch (MilestoneException e) {
            model.addAttribute("error", e.getMessage());
            return "project/detail";
        }
    }
}
