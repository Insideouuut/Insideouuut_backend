package com.goorm.insideout.meeting.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.goorm.insideout.meeting.domain.Meeting;
import com.goorm.insideout.meeting.domain.Progress;
import com.goorm.insideout.meeting.repository.custom.MeetingQueryDslRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Long>, MeetingQueryDslRepository {
	// 내가 호스트인 모임 목록 찾기
	@Query("select m from Meeting m where m.host.id = ?1 and m.progress = ?2")
	Page<Meeting> findRunningMeetings(Long id, Progress progress, Pageable pageable);
}
