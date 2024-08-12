package com.goorm.insideout.meeting.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.goorm.insideout.meeting.domain.MeetingUser;
import com.goorm.insideout.meeting.domain.Progress;

public interface MeetingUserRepository extends JpaRepository<MeetingUser, Long> {
	List<MeetingUser> findByMeetingId(Long meetingId);
	Optional<MeetingUser> findByMeetingIdAndUserId(Long meetingId, Long userId);
	// 나의 모임 참여 목록 찾기 -> 이후 Meeting 객체로 변경
	@Query("select m from MeetingUser m where m.user.id = ?1 and m.meeting.progress = ?2")
	Page<MeetingUser> findOngoingMeetingsByProgress(Long id, Progress progress, Pageable pageable);

	@Query("select m from MeetingUser m where m.user.id = ?1  and m.meeting.progress = ?2")
	List<MeetingUser> findOngoingMeetingsByProgress(Long id, Progress progress);

	// 나의 동아리 모임 참여 목록 찾기
	@Query("select m from MeetingUser m where m.user.id = ?1 and m.meeting.club.clubId = ?2 and m.meeting.progress = ?3")
	List<MeetingUser> findOngoingClubMeetingsByProgress(Long id, Long clubId, Progress progress);

	// 참여중인 모임 중, 이미 종료된 모임 참여 목록 찾기 -> 이후 Meeting 객체로 변경
	@Query("select m from MeetingUser m where m.user.id = ?1 and m.meeting.progress = ?2")
	Page<MeetingUser> findEndedMeetings(Long id, Progress progress, Pageable pageable);

	@Query("select m from MeetingUser m where m.user.id = ?1 and m.meeting.progress = ?2")
	List<MeetingUser> findEndedMeetings(Long id, Progress progress);

}
