package com.nhnacademy.miniDooray.server.adapter;

import com.nhnacademy.miniDooray.server.dto.account.ResponseDto;
import com.nhnacademy.miniDooray.server.dto.milestone.MilestoneRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class Adapter {

    private final RestTemplate restTemplate;

    public <T> ResponseEntity<T> get(String url, Class<T> responseType) {
        return restTemplate.exchange(url, HttpMethod.GET, null, responseType);
    }

    public <T> ResponseEntity<T> getWithHeader(String url, Class<T> responseType, String headerName, String headerValue) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(headerName, headerValue);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
    }

    public <T> ResponseEntity<List<T>> getList(String url, ParameterizedTypeReference<List<T>> responseType) {
        return restTemplate.exchange(url, HttpMethod.GET, null, responseType);
    }

    public <T> ResponseEntity<List<T>> getListWithHeader(String url, ParameterizedTypeReference<List<T>> responseType, String headerName, String headerValue) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(headerName, headerValue);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
    }

    public <T> ResponseEntity<T> post(String url, Object request, Class<T> responseType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> entity = new HttpEntity<>(request, httpHeaders);

        return restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
    }

    public <T> ResponseEntity<T> postWithHeader(String url, Object request, Class<T> responseType, String headerName, String headerValue) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(headerName, headerValue);

        HttpEntity<Object> entity = new HttpEntity<>(request, httpHeaders);

        return restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
    }

    public <T> ResponseEntity<T> patchWithHeader(String url, Object request, Class<T> responseType, String headerName, String headerValue) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(headerName, headerValue);

        HttpEntity<Object> entity = new HttpEntity<>(request, httpHeaders);

        return restTemplate.exchange(url, HttpMethod.PATCH, entity, responseType);
    }

    public <T> ResponseEntity<T> delete(String url, Class<T> responseType) {
        return restTemplate.exchange(url, HttpMethod.DELETE, null, responseType);
    }

    public <T> ResponseEntity<T> patch(String url, Object request, Class<T> responseType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> entity = new HttpEntity<>(request, httpHeaders);

        return restTemplate.exchange(url, HttpMethod.PATCH, entity, responseType);
    }

    public ResponseEntity<ResponseDto> putWithHeader(String url, Object request, Class<ResponseDto> messageResponseDtoClass, String s, String userId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(s, userId);

        HttpEntity<Object> entity = new HttpEntity<>(request, httpHeaders);

        return restTemplate.exchange(url, HttpMethod.PUT, entity, messageResponseDtoClass);
    }

    public ResponseEntity<ResponseDto> deleteWithHeader(String url, Class<ResponseDto> responseDtoClass, String s, String userId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(s, userId);

        HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);

        return restTemplate.exchange(url, HttpMethod.DELETE, entity, responseDtoClass);
    }
}
