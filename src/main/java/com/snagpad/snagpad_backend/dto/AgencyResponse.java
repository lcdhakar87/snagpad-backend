package com.snagpad.snagpad_backend.dto;

import java.time.LocalDateTime;

public record AgencyResponse(
        Long id,
        String name,
        String email,
        boolean active,
        LocalDateTime createdAt
) {}