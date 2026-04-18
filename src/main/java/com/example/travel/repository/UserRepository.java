package com.example.travel.repository;

import org.springframework.data.jpa.repository.JpaRepository; // This was the missing symbol!
import org.springframework.stereotype.Repository;

import com.example.travel.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
