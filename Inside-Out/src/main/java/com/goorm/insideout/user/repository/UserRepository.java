package com.goorm.insideout.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goorm.insideout.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Boolean existsByEmail(String email);
	Boolean existsByNickname(String nickname);
	Optional<User> findByEmail(String email);}
