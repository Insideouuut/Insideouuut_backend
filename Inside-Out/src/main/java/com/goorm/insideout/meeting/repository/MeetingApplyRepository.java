package com.goorm.insideout.meeting.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.goorm.insideout.meeting.domain.MeetingApply;
import com.goorm.insideout.meeting.domain.MeetingUser;
import com.goorm.insideout.meeting.domain.Progress;

public interface MeetingApplyRepository extends JpaRepository<MeetingApply, Long> {
	Optional<MeetingApply> findById(Long applyId);

	List<MeetingApply> findByMeetingId(Long meetingId);

	Optional<MeetingApply> findByMeetingIdAndUserId(Long meetingId, Long userId);

	@Query("select m from MeetingApply m where m.user.id = ?1 and m.meeting.progress = ?2")
	Page<MeetingApply> findOngoingMeetingsByProgress(Long id, Progress progress, Pageable pageable);

	@Query("select m from MeetingApply m where m.user.id = ?1  and m.meeting.progress = ?2")
	List<MeetingApply> findOngoingMeetingsByProgress(Long id, Progress progress);
}
