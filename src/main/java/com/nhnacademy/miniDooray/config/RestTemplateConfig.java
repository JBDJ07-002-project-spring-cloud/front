package com.nhnacademy.miniDooray.config;

import com.nhnacademy.miniDooray.interceptor.UserIdHeaderInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Value("${task-api.url}")
    private String taskApiUrl;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(new UserIdHeaderInterceptor(taskApiUrl));
        return restTemplate;
    }
}
