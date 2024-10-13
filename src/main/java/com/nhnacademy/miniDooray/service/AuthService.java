package com.nhnacademy.miniDooray.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;

    @Value("${account-api.url}")
    private String accountApiUrl;

    public boolean registerUser(String userName, String userPassword, String userEmail) {
        String url = accountApiUrl + "/auth/sign-in";

        Map<String, Object> request = new HashMap<>();
        request.put("userName", userName);
        request.put("userPassword", passwordEncoder.encode(userPassword));
        request.put("userEmail", userEmail);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
                return true;
            } else if (response.getStatusCode() == HttpStatus.CONFLICT) {
                // 아이디 또는 이메일이 이미 존재하는 경우
                return false;
            } else {
                return false;
            }
        } catch (Exception e) {
            // 예외 처리
            return false;
        }
    }

    public boolean withdrawUser(String userName) {
        String url = accountApiUrl + "/auth/withdraw";

        Map<String, String> request = new HashMap<>();
        request.put("userName", userName);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.PATCH, entity, Map.class);

            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            // 예외 처리
            return false;
        }
    }
}
