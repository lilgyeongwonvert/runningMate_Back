package com.demo.album.controller;

import com.demo.album.entity.User;
import com.demo.album.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService; // UserService를 목으로 대체하여 의존성을 격리

    @Test
    public void testRegisterUser() throws Exception {
        User user = new User("testuser", "password", "testnick", "test@example.com");

        // Service mock 설정
        when(userService.registerUser(user.getUsername(), user.getPassword(), user.getNickname(), user.getEmail()))
                .thenReturn(new User(1L, "testuser", "testnick", "test@example.com"));

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.nickname").value("testnick"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    public void testLoginUser() throws Exception {
        User user = new User("testuser", "password");

        // 로그인 성공 시 JWT 토큰이 발급된다고 가정
        when(userService.authenticateUser(user.getUsername(), user.getPassword()))
                .thenReturn(Optional.of(new User(1L, "testuser", "testnick", "test@example.com")));

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty()); // 성공 시 토큰 반환 확인
    }
}
