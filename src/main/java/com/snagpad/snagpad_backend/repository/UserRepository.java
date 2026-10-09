package com.snagpad.snagpad_backend.repository;

import com.snagpad.snagpad_backend.dto.AgencyResponse;
import com.snagpad.snagpad_backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query("  SELECT COUNT(ur) > 0 FROM UserRole ur WHERE ur.user.email = :email AND ur.role.name = :roleName  ")
    boolean existsByEmailAndRole(String email, String roleName);

    @Query(" SELECT DISTINCT u FROM User u  LEFT JOIN FETCH u.userRoles ur  LEFT JOIN FETCH ur.role WHERE u.email = :email ")
    Optional<User> findByEmailWithRoles(@Param("email") String email);

    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN u.userRoles ur " +
            "JOIN ur.role r " +
            "WHERE r.name = :roleName")
    Page<AgencyResponse> findByRoleName(@Param("roleName") String roleName, Pageable pageable);

}
