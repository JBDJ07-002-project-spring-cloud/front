package com.nhnacademy.miniDooray.server.service;

import com.nhnacademy.miniDooray.server.adapter.Adapter;
import com.nhnacademy.miniDooray.server.dto.account.LoginRequest;
import com.nhnacademy.miniDooray.server.dto.account.ResponseDto;
import com.nhnacademy.miniDooray.server.dto.account.UserRegistrationRequest;
import com.nhnacademy.miniDooray.server.exception.account.UserLoginFailedException;
import com.nhnacademy.miniDooray.server.exception.account.UserRegisterFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final Adapter adapter;
    private final PasswordEncoder passwordEncoder;

    @Value("${account-api.url}")
    private String accountApiUrl;

    public ResponseDto registerUser(UserRegistrationRequest userRequest) {
        String url = accountApiUrl + "/auth/sign-in";

        try {
            // userRequest.setUserPassword(passwordEncoder.encode(userRequest.getUserPassword()));

            ResponseEntity<ResponseDto> response = adapter.post(url, userRequest, ResponseDto.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            throw new UserRegisterFailedException("회원가입 실패");
        } catch (HttpClientErrorException e) {
            throw new UserRegisterFailedException("회원가입 실패");
        }
    }

    public ResponseDto loginUser(LoginRequest loginRequest) {
        String url = accountApiUrl + "/auth/login";

        try {
            ResponseEntity<ResponseDto> response = adapter.post(url, loginRequest, ResponseDto.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            throw new UserLoginFailedException("로그인 실패");
        } catch (HttpClientErrorException e) {
            throw new UserLoginFailedException("로그인 실패");
        }
    }

    public boolean withdrawUser(String userName) {
        String url = accountApiUrl + "/auth/withdraw";

        try {
            ResponseEntity<ResponseDto> response = adapter.patch(url, Map.of("userName", userName), ResponseDto.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException e) {
            return false;
        }
    }
}
