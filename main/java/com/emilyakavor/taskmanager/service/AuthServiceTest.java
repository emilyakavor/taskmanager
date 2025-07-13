package com.emilyakavor.taskmanager.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    @Test
    public void testPasswordEncoding() {
        AuthService authService = new AuthService(new BCryptPasswordEncoder());
        String raw = "password123";
        String encoded = authService.encodePassword(raw);

        assertNotNull(encoded);
        assertTrue(authService.matches(raw, encoded));
    }
}