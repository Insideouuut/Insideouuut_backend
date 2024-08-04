package com.goorm.insideout.search.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.goorm.insideout.global.response.ApiResponse;
import com.goorm.insideout.meeting.dto.request.MeetingSearchRequest;
import com.goorm.insideout.meeting.dto.response.MeetingResponse;
import com.goorm.insideout.meeting.service.MeetingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "SearchController", description = "검색 관련 API")
public class SearchController {
	private final MeetingService meetingService;

	@GetMapping("/search/meeting")
	@Operation(summary = "모임 검색 API", description = "모임을 검색하는 API 입니다.")
	public ApiResponse<MeetingResponse> findAll(
		@RequestParam("query") String query,
		@RequestParam("category") String category,
		@RequestParam("sort") String sortType,
		@RequestParam("page") int page
	) {
		MeetingSearchRequest condition = new MeetingSearchRequest(query, category, sortType);
		PageRequest pageRequest = PageRequest.of(page - 1, 20);

		Page<MeetingResponse> searchResult = meetingService.findBySortType(condition, pageRequest);

		return new ApiResponse<>(searchResult);
	}
}
