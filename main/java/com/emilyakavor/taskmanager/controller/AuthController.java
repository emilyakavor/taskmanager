package com.emilyakavor.taskmanager.controller;

import com.emilyakavor.taskmanager.dto.AuthResponse;
import com.emilyakavor.taskmanager.dto.LoginRequest;
import com.emilyakavor.taskmanager.entity.User;
import com.emilyakavor.taskmanager.repository.UserRepository;
import com.emilyakavor.taskmanager.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authManager, JwtService jwtService, UserRepository userRepository) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(auth);

            User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
            String token = jwtService.generateToken(user);

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}