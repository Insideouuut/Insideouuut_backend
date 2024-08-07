package com.goorm.insideout.image.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.goorm.insideout.image.domain.MeetingImage;

public interface MeetingImageRepository extends JpaRepository<MeetingImage, Long> {
	@Query("select m from MeetingImage m where m.meeting.id = ?1")
	List<MeetingImage> findByMeetingId(Long id);
}
