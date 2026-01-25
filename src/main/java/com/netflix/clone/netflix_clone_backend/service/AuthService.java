package com.netflix.clone.netflix_clone_backend.service;

import com.netflix.clone.netflix_clone_backend.dto.request.LoginRequest;
import com.netflix.clone.netflix_clone_backend.dto.request.UserRequest;
import com.netflix.clone.netflix_clone_backend.dto.response.EmailValidationResponse;
import com.netflix.clone.netflix_clone_backend.dto.response.LoginResponse;
import com.netflix.clone.netflix_clone_backend.dto.response.MessageResponse;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

public interface AuthService {
    MessageResponse signup(@Valid UserRequest userRequest);

    LoginResponse login(@Valid LoginRequest loginRequest);

    EmailValidationResponse validateEmail(String email);

    MessageResponse verifyEmail(String token);
}
