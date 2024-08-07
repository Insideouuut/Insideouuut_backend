package com.goorm.insideout.image.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.goorm.insideout.image.domain.ClubImage;
import com.goorm.insideout.image.domain.MeetingImage;

public interface ClubImageRepository extends JpaRepository<ClubImage, Long> {
	@Query("select c from ClubImage c where c.club.id = ?1")
	List<ClubImage> findByClubId(Long id);
}
