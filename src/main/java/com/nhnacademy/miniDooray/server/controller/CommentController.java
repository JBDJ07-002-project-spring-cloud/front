package com.nhnacademy.miniDooray.server.controller;

import com.nhnacademy.miniDooray.server.dto.account.ResponseDto;
import com.nhnacademy.miniDooray.server.dto.comment.CommentCreateRequestDto;
import com.nhnacademy.miniDooray.server.dto.comment.CommentResponseDto;
import com.nhnacademy.miniDooray.server.dto.comment.CommentUpdateRequestDto;
import com.nhnacademy.miniDooray.server.exception.comment.CommentException;
import com.nhnacademy.miniDooray.server.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/task/{projectId}/{taskId}")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comments")
    public String getComments(@PathVariable Long projectId,
                              @PathVariable Long taskId,
                              Model model) {
        try {
            List<CommentResponseDto> comments = commentService.getComments(projectId, taskId);
            model.addAttribute("comments", comments);
            model.addAttribute("projectId", projectId);
            model.addAttribute("taskId", taskId);
            return "comment/list";
        } catch (CommentException e) {
            model.addAttribute("error", e.getMessage());
            return "comment/list";
        }
    }

    @GetMapping("/comment/create")
    public String createCommentForm(@PathVariable Long projectId,
                                    @PathVariable Long taskId,
                                    CommentCreateRequestDto commentCreateRequestDto,
                                    Model model) {
        model.addAttribute("projectId", projectId);
        model.addAttribute("taskId", taskId);
        return "comment/create";
    }

    @PostMapping("/comment/create")
    public String createComment(@PathVariable Long projectId,
                                @PathVariable Long taskId,
                                @Valid @ModelAttribute CommentCreateRequestDto commentRequest,
                                BindingResult bindingResult,
                                Authentication authentication,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("projectId", projectId);
            model.addAttribute("taskId", taskId);
            return "comment/create";
        }

        String userId = authentication.getName();

        try {
            ResponseDto response = commentService.createComment(projectId, taskId, commentRequest, userId);
            return "redirect:/task/" + projectId + "/" + taskId + "/comments";
        } catch (CommentException e) {
            model.addAttribute("error", e.getMessage());
            return "comment/create";
        }
    }

    @GetMapping("/comment/{commentId}/edit")
    public String editCommentForm(@PathVariable Long projectId,
                                  @PathVariable Long taskId,
                                  @PathVariable Long commentId,
                                  CommentUpdateRequestDto commentUpdateRequestDto,
                                  Model model) {
        // 기존 댓글 내용을 가져와서 commentUpdateRequestDto에 설정할 수 있습니다.
        model.addAttribute("projectId", projectId);
        model.addAttribute("taskId", taskId);
        model.addAttribute("commentId", commentId);
        return "comment/edit";
    }

    @PostMapping("/comment/{commentId}/edit")
    public String updateComment(@PathVariable Long projectId,
                                @PathVariable Long taskId,
                                @PathVariable Long commentId,
                                @Valid @ModelAttribute CommentUpdateRequestDto commentRequest,
                                BindingResult bindingResult,
                                Authentication authentication,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("projectId", projectId);
            model.addAttribute("taskId", taskId);
            model.addAttribute("commentId", commentId);
            return "comment/edit";
        }

        String userId = authentication.getName();

        try {
            ResponseDto response = commentService.updateComment(projectId, taskId, commentId, commentRequest, userId);
            return "redirect:/task/" + projectId + "/" + taskId + "/comments";
        } catch (CommentException e) {
            model.addAttribute("error", e.getMessage());
            return "comment/edit";
        }
    }

    @PostMapping("/comment/{commentId}/delete")
    public String deleteComment(@PathVariable Long projectId,
                                @PathVariable Long taskId,
                                @PathVariable Long commentId,
                                Authentication authentication,
                                Model model) {
        String userId = authentication.getName();

        try {
            ResponseDto response = commentService.deleteComment(projectId, taskId, commentId, userId);
            return "redirect:/task/" + projectId + "/" + taskId + "/comments";
        } catch (CommentException e) {
            model.addAttribute("error", e.getMessage());
            return "comment/list";
        }
    }
}
