package com.goorm.insideout.meeting.repository.custom;

import java.util.List;

import com.goorm.insideout.meeting.dto.request.MeetingSearchRequest;
import com.goorm.insideout.meeting.dto.response.MeetingResponse;

public interface MeetingQueryDslRepository {

	List<MeetingResponse> findAllByCondition(MeetingSearchRequest condition);

	List<MeetingResponse> findByConditionAndSortType(MeetingSearchRequest condition);

	List<MeetingResponse> findBySortType(MeetingSearchRequest condition);
}
