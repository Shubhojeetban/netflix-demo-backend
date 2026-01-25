package com.netflix.clone.netflix_clone_backend.serviceImpl;

import com.netflix.clone.netflix_clone_backend.exception.EmailNotVerifiedException;
import com.netflix.clone.netflix_clone_backend.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.frontend.url:http://localhost:4200}")
    private String frontendURL;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendVerificationEmail(String toEmail, String token) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(fromEmail);
            simpleMailMessage.setTo(toEmail);
            simpleMailMessage.setSubject("Netflix clone - Verify email");

            String verificationLink = frontendURL+ "/verify-email?token=" + token;
            String emailBody = String.format(
                    "Hello,\n\n" +
                            "Welcome to Netflix Clone!\n\n" +
                            "Thank you for signing up. To complete your registration, please verify your email address by clicking the link below:\n\n" +
                            "%s\n\n" +
                            "If you did not create this account, you can safely ignore this email.\n\n" +
                            "This verification link is valid for a limited time.\n\n" +
                            "Happy streaming!\n" +
                            "Netflix Clone Team",
                    verificationLink
            );
            simpleMailMessage.setText(emailBody);
            mailSender.send(simpleMailMessage);
            LOGGER.info("Verification email sent to: {}", toEmail);
        } catch (Exception ex) {
            LOGGER.error("Failed to send password reset email to {}: {} ", toEmail, ex.getMessage(), ex);
            throw new EmailNotVerifiedException("Failed to send verification email");
        }
    }

    @Override
    public void sendPasswordResetEmail(String toEmail, String token) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(fromEmail);
            simpleMailMessage.setTo(toEmail);
            simpleMailMessage.setSubject("Netflix clone - Password reset");

            String resentLink = frontendURL+ "/reset-password?token=" + token;
            String emailBody = String.format(
                    "Hello,\n\n" +
                            "We received a request to reset your Netflix Clone account password.\n\n" +
                            "To reset your password, please click the link below:\n\n" +
                            "%s\n\n" +
                            "If you did not request a password reset, please ignore this email. Your password will remain unchanged.\n\n" +
                            "This password reset link is valid for 1 hour.\n\n" +
                            "Stay safe and happy streaming!\n" +
                            "Netflix Clone Team",
                    resentLink
            );
            simpleMailMessage.setText(emailBody);
            mailSender.send(simpleMailMessage);
            LOGGER.info("Password reset email sent to: {}", toEmail);
        } catch (Exception ex) {
            LOGGER.error("Failed to send password reset email to {}: {} ", toEmail, ex.getMessage(), ex);
            throw new EmailNotVerifiedException("Failed to send password reset email");
        }
    }
}
