package com.nhnacademy.miniDooray.controller;

import com.nhnacademy.miniDooray.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
    public String registerForm(Model model) {
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
            return "redirect:/register?error";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String userId, @RequestParam String password, HttpSession httpSession) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userId, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            httpSession.setAttribute("userId", userId);

            return "redirect:/dashboard";
        } catch (AuthenticationException e) {
            return "redirect:/login?error";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login?logout";
    }
}
