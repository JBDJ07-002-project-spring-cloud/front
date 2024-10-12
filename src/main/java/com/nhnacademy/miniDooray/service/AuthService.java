package com.nhnacademy.miniDooray.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RestTemplate restTemplate;
    private final String accountApiUrl = "http://localhost:8081";

    public boolean registerUser(String userId, String userPassword, String userEmail) {
        String url = accountApiUrl + "/auth/sign-in";

        Map<String, String> request = new HashMap<>();
        request.put("userId", userId);
        request.put("userPassword", userPassword);
        request.put("userEmail", userEmail);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        return response.getStatusCode() == HttpStatus.OK;
    }
}
