package com.nhnacademy.miniDooray.service;

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
        String userId = authentication.getName();
        String password = (String) authentication.getCredentials();

        String url = accountApiUrl + "/auth/user/" + userId;

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> userMap = response.getBody();
                String encodedPassword = (String) userMap.get("userPassword");

                if (passwordEncoder.matches(password, encodedPassword)) {
                    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
                    return new UsernamePasswordAuthenticationToken(userId, null, authorities);
                } else {
                    throw new BadCredentialsException("Invalid username or password");
                }
            } else {
                throw new BadCredentialsException("Invalid username or password");
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
