package com.nhnacademy.miniDooray.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final RestTemplate restTemplate;
    private final String accountApiUrl = "http://localhost:8081";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String url = accountApiUrl + "/auth/user/" + username;

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> userMap = response.getBody();

            String userId = (String) userMap.get("userId");
            String password = (String) userMap.get("userPassword");

            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

            return new org.springframework.security.core.userdetails.User(userId, password, authorities);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public boolean authenticate(String userId, String password) {
        String url = accountApiUrl + "/auth/login";

        Map<String, String> request = new HashMap<>();
        request.put("userId", userId);
        request.put("userPw", password);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        return response.getStatusCode() == HttpStatus.OK;
    }

}
