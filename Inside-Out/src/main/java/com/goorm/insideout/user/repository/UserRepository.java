package com.goorm.insideout.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goorm.insideout.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Boolean existsByEmail(String email);

	Boolean existsByNickname(String nickname);

	@Query("SELECT u FROM User u LEFT JOIN FETCH u.profileImage WHERE u.id = :userId")
	User findByIdWithProfileImage(@Param("userId") Long userId);

	@Query("SELECT u FROM User u LEFT JOIN FETCH u.profileImage LEFT JOIN FETCH u.interests LEFT JOIN FETCH u.locations WHERE u.email = :email")
	User findByEmailWithDetails(@Param("email") String email);

	Optional<User> findByEmail(String email);

}
