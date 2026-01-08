package com.netflix.clone.netflix_clone_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotBlank
    private String token;
    @NotBlank
    @Size(min = 6, message = "New password must have at lease {min} characters")
    private String newPassword;
}
