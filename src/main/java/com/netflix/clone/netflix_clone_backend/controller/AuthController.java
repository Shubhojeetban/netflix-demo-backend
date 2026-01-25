package com.netflix.clone.netflix_clone_backend.controller;

import com.netflix.clone.netflix_clone_backend.dto.request.LoginRequest;
import com.netflix.clone.netflix_clone_backend.dto.request.UserRequest;
import com.netflix.clone.netflix_clone_backend.dto.response.EmailValidationResponse;
import com.netflix.clone.netflix_clone_backend.dto.response.LoginResponse;
import com.netflix.clone.netflix_clone_backend.dto.response.MessageResponse;
import com.netflix.clone.netflix_clone_backend.service.AuthService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(authService.signup(userRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @GetMapping("/validate-email")
    public ResponseEntity<EmailValidationResponse> validateEmail(@RequestParam String email) {
        return ResponseEntity.ok(authService.validateEmail(email));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<MessageResponse> verifyEmail(@RequestParam String token) {
        return ResponseEntity.ok(authService.verifyEmail(token));
    }
}
