package com.emilyakavor.taskmanager.service;

import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthService {

    private final PasswordEncoder encoder;

    public AuthService(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public String encodePassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }


    @PreAuthorize("permitAll()")
    public Authentication authenticate(String email, String password) {
        return new UsernamePasswordAuthenticationToken(email, password);
    }

}