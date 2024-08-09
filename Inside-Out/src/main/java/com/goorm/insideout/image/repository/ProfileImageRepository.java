package com.goorm.insideout.image.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.goorm.insideout.club.entity.ClubUser;
import com.goorm.insideout.image.domain.ProfileImage;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
	Optional<ProfileImage> findByImage_StoreName(String storeName);

	@Query("select p from ProfileImage p where p.user.id = ?1")
	Optional<ProfileImage> findByUserId(Long id);

	@Query("SELECT p FROM ProfileImage p JOIN FETCH p.user WHERE p.user.id = :userId")
	Optional<ProfileImage> findByUserIdJQL(Long userId);
/*
	@EntityGraph(attributePaths = { "url" })
	Optional<String> fetchProfileImage();

 */
}
