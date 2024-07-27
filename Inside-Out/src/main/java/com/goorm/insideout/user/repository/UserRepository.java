package com.goorm.insideout.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goorm.insideout.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Boolean existsByEmail(String email);
	Optional<User> findByEmail(String email);
}