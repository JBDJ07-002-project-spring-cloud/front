package com.nhnacademy.miniDooray;

import com.nhnacademy.miniDooray.controller.AuthController;
import com.nhnacademy.miniDooray.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("회원가입 성공 테스트")
    public void registerSuccessTest() throws Exception {
        when(authService.registerUser("user1", "password1", "user1@test.com")).thenReturn(true);

        mockMvc.perform(post("/register")
                .param("userId", "user1")
                .param("userPassword", "password1")
                .param("userEmail", "user1@test.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @DisplayName("회원가입 실패 테스트")
    public void registerFailureTest() throws Exception {
        when(authService.registerUser("user1", "password1", "user1@test.com")).thenReturn(false);

        mockMvc.perform(post("/register")
                .param("userId", "user1")
                .param("userPassword", "password1")
                .param("userEmail", "user1@test.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register?error"));
    }
}
