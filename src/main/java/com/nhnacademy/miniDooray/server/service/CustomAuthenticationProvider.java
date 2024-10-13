package com.nhnacademy.miniDooray.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;

    @Value("${account-api.url}")
    private String accountApiUrl;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String userPassword = (String) authentication.getCredentials();

        String url = accountApiUrl + "/auth/login";

        Map<String, String> request = new HashMap<>();
        request.put("userName", userName);
        request.put("userPw", userPassword);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                Long userId = ((Number) responseBody.get("userId")).longValue();

                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
                return new UsernamePasswordAuthenticationToken(userName, null, authorities);
            } else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new BadCredentialsException("Invalid username or password");
            } else {
                throw new AuthenticationServiceException("Authentication service unavailable");
            }
        } catch (Exception e) {
            throw new AuthenticationServiceException("Authentication service unavailable", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
