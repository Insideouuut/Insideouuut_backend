package com.goorm.insideout.meeting.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.goorm.insideout.meeting.domain.ApprovalStatus;
import com.goorm.insideout.meeting.domain.MeetingUser;
import com.goorm.insideout.meeting.domain.Progress;

public interface MeetingUserRepository extends JpaRepository<MeetingUser, Long> {
	// 나의 모임 참여 목록 찾기 -> 이후 Meeting 객체로 변경
	@Query("select m from MeetingUser m where m.user.id = ?1 and m.approvalStatus = ?2 and m.meeting.progress = ?3")
	Page<MeetingUser> findOngoingMeetingsByProgress(Long id, ApprovalStatus approvalStatus,
		Progress progress, Pageable pageable);

	// 참여중인 모임 중, 이미 종료된 모임 참여 목록 찾기 -> 이후 Meeting 객체로 변경
	@Query("select m from MeetingUser m where m.user.id = ?1 and m.approvalStatus = ?2 and m.meeting.progress = ?3")
	Page<MeetingUser> findEndedMeetings(Long id, ApprovalStatus approvalStatus, Progress progress, Pageable pageable);


}
