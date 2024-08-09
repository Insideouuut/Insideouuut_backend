package com.goorm.insideout.club.repository.custom;

import java.util.List;

import com.goorm.insideout.club.dto.responseDto.ClubBoardResponseDto;
import com.goorm.insideout.meeting.dto.request.SearchRequest;

public interface ClubQueryDslRepository {

	List<ClubBoardResponseDto> findAllByCondition(SearchRequest condition);

	List<ClubBoardResponseDto> findByConditionAndSortType(SearchRequest condition);

	List<ClubBoardResponseDto> findBySortType(SearchRequest condition);
}
