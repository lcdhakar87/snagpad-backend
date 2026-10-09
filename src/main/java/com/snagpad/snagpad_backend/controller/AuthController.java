package com.snagpad.snagpad_backend.controller;

import com.snagpad.snagpad_backend.dto.AgencyCreateRequest;
import com.snagpad.snagpad_backend.dto.LoginRequest;
import com.snagpad.snagpad_backend.dto.LoginResponse;
import com.snagpad.snagpad_backend.dto.RegisterRequest;
import com.snagpad.snagpad_backend.entity.User;
import com.snagpad.snagpad_backend.exception.EmailAlreadyExistsException;
import com.snagpad.snagpad_backend.repository.UserRepository;
import com.snagpad.snagpad_backend.repository.UserRoleRepository;
import com.snagpad.snagpad_backend.security.CustomUserDetailsService;
import com.snagpad.snagpad_backend.security.JwtService;
import com.snagpad.snagpad_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRoleRepository userRoleRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

        boolean hasRole = user.getUserRoles()
                .stream()
                .anyMatch(userRole ->
                        userRole.getRole().getName().equalsIgnoreCase(request.getRole()));

        if (!hasRole) {
            throw new AccessDeniedException("You are not authorized as " + request.getRole());
        }


        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(userDetails);


        return ResponseEntity.status(HttpStatus.OK).body( new LoginResponse(token, request.getRole()) );
    }

    @PostMapping("/register")
    public ResponseEntity<String> register( @Valid @RequestBody AgencyCreateRequest request) {

        if (userRepository.existsByEmailAndRole(request.email(), request.role())) {
            throw new EmailAlreadyExistsException("User already registered with this role");
        }
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }
}