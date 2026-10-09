package com.snagpad.snagpad_backend.controller;

import com.snagpad.snagpad_backend.dto.AgencyCreateRequest;
import com.snagpad.snagpad_backend.dto.AgencyResponse;
import com.snagpad.snagpad_backend.dto.RegisterRequest;
import com.snagpad.snagpad_backend.entity.User;
import com.snagpad.snagpad_backend.exception.EmailAlreadyExistsException;
import com.snagpad.snagpad_backend.repository.UserRepository;
import com.snagpad.snagpad_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountsController {

    @Autowired
    private final UserService userService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<User> createAccount(@Valid @RequestBody AgencyCreateRequest request) {

        if (userRepository.existsByEmailAndRole(request.email(), request.role())) {
            throw new EmailAlreadyExistsException("User already registered with this role");
        }
        User acc = userService.register(request);
        return ResponseEntity.ok(acc) ;
    }

    @GetMapping("/agency")
    public ResponseEntity<Page<AgencyResponse>> getAllAgency(@PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable) {
        Page<AgencyResponse> usersPage = userService.getAllUsers(pageable);
        return ResponseEntity.ok(usersPage);
    }

}
