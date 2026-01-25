package com.netflix.clone.netflix_clone_backend.serviceImpl;

import com.netflix.clone.netflix_clone_backend.dao.UserRepository;
import com.netflix.clone.netflix_clone_backend.dto.request.LoginRequest;
import com.netflix.clone.netflix_clone_backend.dto.request.UserRequest;
import com.netflix.clone.netflix_clone_backend.dto.response.EmailValidationResponse;
import com.netflix.clone.netflix_clone_backend.dto.response.LoginResponse;
import com.netflix.clone.netflix_clone_backend.dto.response.MessageResponse;
import com.netflix.clone.netflix_clone_backend.entity.User;
import com.netflix.clone.netflix_clone_backend.enums.Role;
import com.netflix.clone.netflix_clone_backend.exception.*;
import com.netflix.clone.netflix_clone_backend.security.JwtUtil;
import com.netflix.clone.netflix_clone_backend.service.AuthService;
import com.netflix.clone.netflix_clone_backend.service.EmailService;
import com.netflix.clone.netflix_clone_backend.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final ServiceUtil serviceUtil;

    @Override
    public MessageResponse signup(UserRequest userRequest) {
        if(userRepository.existsByEmail(userRequest.getEmail()) && userRepository.findByEmail(userRequest.getEmail()).get().isEmailVerified()) {
            throw new EmailAlreadyExistsException("Email already exists: "+ userRequest.getEmail());
        }
        String verificationToken = UUID.randomUUID().toString();
        User user = User.builder()
                        .email(userRequest.getEmail())
                        .password(passwordEncoder.encode(userRequest.getPassword()))
                        .fullName(userRequest.getFullName())
                        .role(Role.USER)
                        .active(true)
                        .emailVerified(false)
                        .verificationToken(verificationToken)
                        .verificationTokenExpiry(Instant.now().plusSeconds(86400))
                        .build();

        userRepository.save(user);
        emailService.sendVerificationEmail(userRequest.getEmail(), verificationToken);

        return new MessageResponse("Registration successful! Please check your email to verify your account");
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .filter(u -> passwordEncoder.matches(loginRequest.getPassword(), u.getPassword()))
                .orElseThrow(() -> new BadCredentialException("Invalid email or password"));

        if(!user.isActive()) throw new AccountDeactivatedException("Your account is deactivated! Please contact for support.");
        if(!user.isEmailVerified()) throw new EmailNotVerifiedException("Please verify your account before logging in. Check your inbox for verification link.");

        final String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new LoginResponse(token, user.getEmail(), user.getFullName(), user.getRole().name());
    }

    @Override
    public EmailValidationResponse validateEmail(String email) {
        boolean exists = userRepository.existsByEmail(email);
        return new EmailValidationResponse(exists, !exists);
    }

    @Override
    public MessageResponse verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid or expired verification token."));

        if(user.getVerificationTokenExpiry() == null || user.getVerificationTokenExpiry().isBefore(Instant.now())) {
            throw  new InvalidTokenException("Verification token has expired. Please request a new one.");
        }

        user.setEmailVerified(true);
        user.setVerificationTokenExpiry(null);
        user.setVerificationToken(null);

        userRepository.save(user);

        return new MessageResponse("Email verified successful! you can now login");
    }
}
