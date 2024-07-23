package com.goorm.insideout.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goorm.insideout.meeting.domain.Meeting;
import com.goorm.insideout.meeting.repository.custom.MeetingQueryDslRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Long>, MeetingQueryDslRepository {
}
