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
            @RequestParam String userId,
            @RequestParam String userPassword,
            @RequestParam String userEmail
    ) {
        boolean success = authService.registerUser(userId, userPassword, userEmail);
        if (success) {
            return "redirect:/login";
        } else {
            return "redirect:/register?error=회원가입 실패. 다시 시도해주세요.";
        }
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password
    ) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return "redirect:/dashboard";
        } catch (AuthenticationException e) {
            return "redirect:/login?error";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout";
    }
}
