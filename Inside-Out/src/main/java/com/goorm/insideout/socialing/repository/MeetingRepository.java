package com.goorm.insideout.socialing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goorm.insideout.socialing.domain.Meeting;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}
