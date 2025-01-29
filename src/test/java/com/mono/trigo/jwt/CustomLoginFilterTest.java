package com.mono.trigo.jwt;

import com.mono.trigo.domain.user.entity.Refresh;
import com.mono.trigo.domain.user.repository.RefreshRepository;

import com.mono.trigo.web.jwt.JWTUtil;
import com.mono.trigo.web.jwt.CustomLoginFilter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


import static io.jsonwebtoken.Jwts.header;
import static org.mockito.Mockito.*;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CustomLoginFilterTest {

    @InjectMocks
    private CustomLoginFilter customLoginFilter;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private RefreshRepository refreshRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customLoginFilter).build();
    }

//    @Test
//    @DisplayName("로그인 성공")
//    void login_Success() throws Exception {
//        // Given
//        String username = "testUser";
//        String password = "password";
//        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
//
//        when(authenticationManager.authenticate(authToken)).thenReturn(authToken);
//        when(jwtUtil.createJwt(eq("access"), anyString(), anyString(), anyLong())).thenReturn("access-token");
//        when(jwtUtil.createJwt(eq("refresh"), anyString(), anyString(), anyLong())).thenReturn("refresh-token");
//
//        MockHttpServletRequestBuilder request = post("/api/v1/users/login")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .param("username", username)
//                .param("password", password);
//
//        // When & Then
//        mockMvc.perform(request)
//                .andExpect(status().isOk())
//                .andExpect(header().exists("access"))
//                .andExpect(cookie().exists("refresh"));
//
//        verify(refreshRepository, times(1)).save(any(Refresh.class));
//    }
//
//    @Test
//    @DisplayName("로그인 실패: 잘못된 인증 정보")
//    void login_Fail_InvalidCredentials() throws Exception {
//        // Given
//        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));
//
//        MockHttpServletRequestBuilder request = post("/api/v1/users/login")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .param("username", "wrongUser")
//                .param("password", "wrongPassword");
//
//        // When & Then
//        mockMvc.perform(request).andExpect(status().isUnauthorized());
//
//        verify(refreshRepository, never()).save(any());
//    }
}
