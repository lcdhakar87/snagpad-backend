package com.snagpad.snagpad_backend.service;

import com.snagpad.snagpad_backend.dto.AgencyCreateRequest;
import com.snagpad.snagpad_backend.dto.AgencyResponse;
import com.snagpad.snagpad_backend.dto.RegisterRequest;
import com.snagpad.snagpad_backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User register(AgencyCreateRequest request);

    Page<AgencyResponse> getAllUsers(Pageable pageable);
}