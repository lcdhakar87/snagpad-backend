package com.snagpad.snagpad_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AgencyCreateRequest(
        @NotBlank(message = "Name is required")
        String name,

        @Email(message = "Invalid email")
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Password is required")
        String password,

        @NotBlank(message = "Role is required")
        String role,

        String title,
        String phone,
        String description,
        String specification,
        String logo
) { }
