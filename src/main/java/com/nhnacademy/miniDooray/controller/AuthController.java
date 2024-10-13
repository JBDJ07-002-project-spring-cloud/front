package com.nhnacademy.miniDooray.controller;

import com.nhnacademy.miniDooray.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    @GetMapping("/login")
    public String loginForm(Model model, @RequestParam(required = false) String error) {
        if (error != null) {
            model.addAttribute("error", "로그인 실패. 아이디와 비밀번호를 확인하세요.");
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model, @RequestParam(required = false) String error) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String userName,
            @RequestParam String userPassword,
            @RequestParam String userEmail,
            Model model
    ) {
        boolean success = authService.registerUser(userName, userPassword, userEmail);
        if (success) {
            return "redirect:/auth/login";
        } else {
            model.addAttribute("error", "회원가입 실패. 다시 시도해주세요.");
            return "register";
        }
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String userName,
            @RequestParam String userPassword,
            Model model
    ) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userName, userPassword)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return "redirect:/dashboard";
        } catch (AuthenticationException e) {
            model.addAttribute("error", "로그인 실패. 아이디와 비밀번호를 확인하세요.");
            return "login";
        }
    }

    @PostMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "redirect:/auth/login?logout";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String userName, Model model) {
        boolean success = authService.withdrawUser(userName);
        if (success) {
            SecurityContextHolder.clearContext();
            return "redirect:/auth/login?withdraw=success";
        } else {
            model.addAttribute("error", "회원탈퇴 실패. 다시 시도해주세요.");
            return "dashboard";
        }
    }
}
