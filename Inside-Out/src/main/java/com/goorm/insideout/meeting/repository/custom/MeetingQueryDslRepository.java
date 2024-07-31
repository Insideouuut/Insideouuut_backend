package com.goorm.insideout.meeting.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.goorm.insideout.meeting.dto.request.MeetingSearchRequest;
import com.goorm.insideout.meeting.dto.response.MeetingResponse;

public interface MeetingQueryDslRepository {

	Page<MeetingResponse> findAllByCondition(MeetingSearchRequest condition, Pageable pageable);

	Page<MeetingResponse> findAllBySortType(MeetingSearchRequest condition, Pageable pageable);
}
