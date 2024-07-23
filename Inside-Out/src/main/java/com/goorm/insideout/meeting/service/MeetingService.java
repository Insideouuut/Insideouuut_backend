package com.goorm.insideout.meeting.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.insideout.meeting.dto.request.MeetingSearchRequest;
import com.goorm.insideout.meeting.dto.response.MeetingResponse;
import com.goorm.insideout.meeting.repository.MeetingRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingService {
	private final MeetingRepository meetingRepository;

	public Page<MeetingResponse> findAllByCondition(MeetingSearchRequest condition, Pageable pageable) {
		return meetingRepository.findAllByCondition(condition, pageable);
	}

	public Page<MeetingResponse> findAllBySortType(MeetingSearchRequest condition, Pageable pageable) {
		return meetingRepository.findAllBySortType(condition, pageable);
	}
}