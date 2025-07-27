package com.cdac.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cdac.entities.User;
import com.cdac.entities.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByRole(UserRole role);

} 