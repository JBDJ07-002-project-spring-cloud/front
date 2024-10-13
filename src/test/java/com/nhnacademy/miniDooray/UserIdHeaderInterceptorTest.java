package com.nhnacademy.miniDooray;

import com.nhnacademy.miniDooray.interceptor.UserIdHeaderInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class UserIdHeaderInterceptorTest {

    private UserIdHeaderInterceptor interceptor;
    private String taskApiUrl = "http://localhost:8082";

    @BeforeEach
    public void setUp() {
        interceptor = new UserIdHeaderInterceptor(taskApiUrl);
    }

    @Test
    public void testUserIdHeaderAdded() throws IOException {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getName()).thenReturn("testUser");

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(interceptor);

        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        mockServer.expect(requestTo(taskApiUrl + "/some-endpoint"))
                .andExpect(header("X-USER-ID", "testUser"))
                .andRespond(withSuccess());

        restTemplate.getForObject(taskApiUrl + "/some-endpoint", String.class);
        mockServer.verify();
    }
}
