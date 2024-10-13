package com.nhnacademy.miniDooray.interceptor;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

public class UserIdHeaderInterceptor implements ClientHttpRequestInterceptor {

    private final String taskApiUrl;

    public UserIdHeaderInterceptor(String taskApiUrl) {
        this.taskApiUrl = taskApiUrl;
    }

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {

        // 요청 URI가 task-api로 시작하는지 확인
        if (request.getURI().toString().startsWith(taskApiUrl)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String userId = authentication.getName();
                if (userId != null) {
                    request.getHeaders().add("X-USER-ID", userId);
                }
            }
        }
        return execution.execute(request, body);
    }
}
