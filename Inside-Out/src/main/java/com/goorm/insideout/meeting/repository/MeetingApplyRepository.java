package com.goorm.insideout.meeting.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goorm.insideout.meeting.domain.MeetingApply;

public interface MeetingApplyRepository extends JpaRepository<MeetingApply, Long> {
	Optional<MeetingApply> findById(Long applyId);
	List<MeetingApply> findByMeetingId(Long meetingId);
}
