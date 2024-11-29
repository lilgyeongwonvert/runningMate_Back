package com.demo.album.service;

import com.demo.album.entity.User;
import com.demo.album.repository.UserRepository;
import com.demo.album.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void testAuthenticateUser() {
        String username = "testuser";
        String password = "password";

        User mockUser = new User(username, passwordEncoder.encode(password), "testnick", "test@example.com");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(password, mockUser.getPassword())).thenReturn(true);

        Optional<User> authenticatedUser = userService.authenticateUser(username, password);
        assertTrue(authenticatedUser.isPresent());
    }
}
