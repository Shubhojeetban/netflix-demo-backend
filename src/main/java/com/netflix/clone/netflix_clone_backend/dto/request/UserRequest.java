package com.netflix.clone.netflix_clone_backend.dto.request;

import lombok.Data;

@Data
public class UserRequest {
    private String email;
    private String password;
    private String fullName;
    private String role;
    private Boolean active;
}
