package com.goorm.insideout.search.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.goorm.insideout.global.response.ApiResponse;
import com.goorm.insideout.meeting.dto.request.SearchRequest;
import com.goorm.insideout.meeting.dto.response.MeetingResponse;
import com.goorm.insideout.meeting.service.MeetingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "SearchController", description = "검색 관련 API")
public class SearchController {
	private final MeetingService meetingService;

	@GetMapping("/search/club")
	@Operation(summary = "동아리 검색 API", description = "동아리를 검색하는 API 입니다.")
	public ApiResponse<MeetingResponse> findClubs(
		@RequestParam("query") String query,
		@RequestParam("category") String category,
		@RequestParam("sort") String sortType
	) {
		SearchRequest condition = new SearchRequest(query, category, sortType);

		if (query.equals("all") && category.equals("all")) {
			return new ApiResponse<>(meetingService.findBySortType(condition));
		}

		return new ApiResponse<>(meetingService.findByConditionAndSortType(condition));
	}

	@GetMapping("/search/meeting")
	@Operation(summary = "모임 검색 API", description = "모임을 검색하는 API 입니다.")
	public ApiResponse<MeetingResponse> findMeetings(
		@RequestParam("query") String query,
		@RequestParam("category") String category,
		@RequestParam("sort") String sortType
	) {
		SearchRequest condition = new SearchRequest(query, category, sortType);

		if (query.equals("all") && category.equals("all")) {
			return new ApiResponse<>(meetingService.findBySortType(condition));
		}

		return new ApiResponse<>(meetingService.findByConditionAndSortType(condition));
	}

	@GetMapping("/search/all")
	@Operation(summary = "동아리 및 모임 검색 API", description = "동아리와 모임을 한꺼번에 검색하는 API 입니다.")
	public ApiResponse<MeetingResponse> findAll(
		@RequestParam("query") String query,
		@RequestParam("category") String category,
		@RequestParam("sort") String sortType
	) {
		SearchRequest condition = new SearchRequest(query, category, sortType);

		if (query.equals("all") && category.equals("all")) {
			return new ApiResponse<>(meetingService.findBySortType(condition));
		}

		return new ApiResponse<>(meetingService.findByConditionAndSortType(condition));
	}
}
