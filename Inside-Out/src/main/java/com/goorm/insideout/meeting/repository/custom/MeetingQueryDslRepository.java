package com.goorm.insideout.meeting.repository.custom;

import java.util.List;

import com.goorm.insideout.meeting.dto.request.SearchRequest;
import com.goorm.insideout.meeting.dto.response.MeetingResponse;

public interface MeetingQueryDslRepository {

	List<MeetingResponse> findAllByCondition(SearchRequest condition);

	List<MeetingResponse> findByConditionAndSortType(SearchRequest condition);

	List<MeetingResponse> findBySortType(SearchRequest condition);
}
