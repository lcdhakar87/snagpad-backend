package com.snagpad.snagpad_backend.service.impl;

import com.snagpad.snagpad_backend.dto.AgencyCreateRequest;
import com.snagpad.snagpad_backend.dto.AgencyResponse;
import com.snagpad.snagpad_backend.dto.RegisterRequest;
import com.snagpad.snagpad_backend.entity.Role;
import com.snagpad.snagpad_backend.entity.User;
import com.snagpad.snagpad_backend.entity.UserRole;
import com.snagpad.snagpad_backend.repository.RoleRepository;
import com.snagpad.snagpad_backend.repository.UserRepository;
import com.snagpad.snagpad_backend.repository.UserRoleRepository;
import com.snagpad.snagpad_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public User register(AgencyCreateRequest request) {

        User user_exist = userRepository.findByEmail(request.email()).orElse(null);
        if (user_exist == null) {
            User user = new User();
            user.setName(request.name());
            user.setEmail(request.email());
            user.setEmail(request.phone());
            user.setPassword(passwordEncoder.encode(request.password()));
            user.setActive(true);
            user_exist = userRepository.save(user);
        }
        Role role = roleRepository.findByName(request.role())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        boolean role_exists = userRepository.existsByEmailAndRole(request.email(), request.role());
        if (role_exists) {
            throw new RuntimeException("User already registered with this role");
        }else{
            UserRole userRole = new UserRole();
            userRole.setUser(user_exist);
            userRole.setRole(role);
            userRole.setCreatedAt(LocalDateTime.now());
            userRoleRepository.save(userRole);

            user_exist = userRepository.findByEmail(request.email()).orElse(null);
        }
        return user_exist;
    }

    public void agencyUpdate(){

    }

    public Page<AgencyResponse> getAllUsers(Pageable pageable) {
        return userRepository.findByRoleName("AGENCY", pageable);
    }



}