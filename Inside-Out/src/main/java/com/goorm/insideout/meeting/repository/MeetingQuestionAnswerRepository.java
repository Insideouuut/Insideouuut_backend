package com.goorm.insideout.meeting.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goorm.insideout.meeting.domain.MeetingQuestionAnswer;

public interface MeetingQuestionAnswerRepository extends JpaRepository<MeetingQuestionAnswer, Long> {
	List<MeetingQuestionAnswer> findByMeetingApplyId(Long applyId);
}
