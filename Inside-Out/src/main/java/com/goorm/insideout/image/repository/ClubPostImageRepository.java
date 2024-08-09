package com.goorm.insideout.image.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.goorm.insideout.image.domain.ClubImage;
import com.goorm.insideout.image.domain.ClubPostImage;

public interface ClubPostImageRepository extends JpaRepository<ClubPostImage, Long> {
	@Query("select c from ClubPostImage c where c.id = ?1")
	List<ClubPostImage> findByClubPostId(Long id);
}
