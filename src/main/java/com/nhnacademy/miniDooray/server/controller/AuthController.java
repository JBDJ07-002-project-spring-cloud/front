package com.nhnacademy.miniDooray.server.controller;

import com.nhnacademy.miniDooray.server.dto.account.LoginRequest;
import com.nhnacademy.miniDooray.server.dto.account.ResponseDto;
import com.nhnacademy.miniDooray.server.dto.account.UserRegistrationRequest;
import com.nhnacademy.miniDooray.server.exception.account.UserLoginFailedException;
import com.nhnacademy.miniDooray.server.exception.account.UserRegisterFailedException;
import com.nhnacademy.miniDooray.server.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
            model.addAttribute("error", error);
        }
        model.addAttribute("loginRequest", new LoginRequest());
        return "auth/login";
    }


    @GetMapping("/sign-in")
    public String signInForm(Model model, @RequestParam(required = false) String error) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "auth/sign-in";
    }


    @GetMapping("/register")
    public String registerForm(Model model, @RequestParam(required = false) String error) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "register";
    }

    @PostMapping("/sign-in")
    public String register(
            @Valid @ModelAttribute UserRegistrationRequest userRequest,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            ResponseDto response = authService.registerUser(userRequest);
            return "redirect:/auth/login";
        } catch (UserRegisterFailedException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute LoginRequest loginRequest,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUserName(), loginRequest.getUserPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            authService.loginUser(loginRequest);
            return "redirect:/dashboard";
        } catch (AuthenticationException e) {
            model.addAttribute("error", "로그인 실패. 아이디와 비밀번호를 확인하세요.");
            return "login";
        } catch (UserLoginFailedException e) {
            model.addAttribute("error", e.getMessage());
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
