package com.goorm.insideout.meeting.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goorm.insideout.auth.dto.CustomUserDetails;

import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.response.ApiResponse;
import com.goorm.insideout.meeting.dto.response.MeetingUserResponse;
import com.goorm.insideout.meeting.service.MeetingUserService;
import com.goorm.insideout.user.domain.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/meetings")
@RequiredArgsConstructor
@Tag(name = "MeetingUserController", description = "모임 멤버 관련 API")
public class MeetingUserController {
	private final MeetingUserService meetingUserService;

	@DeleteMapping("/members/{meetingUserId}/expel")
	@Operation(summary = "모임 추방 API", description = "모임을 추방하는 API입니다.")
	public ApiResponse memberExpel(@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable("meetingUserId") Long meetingUserId) {
		User host = userDetails.getUser();
		meetingUserService.meetingUserExpel(meetingUserId, host);
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	@GetMapping("/{meetingId}/members")
	@Operation(summary = "모임 멤버 조회 API", description = "모임 멤버를 조회하는 API입니다.")
	public ApiResponse<List<MeetingUserResponse>> getMemberList(@PathVariable("meetingId") Long meetingId) {
		List<MeetingUserResponse> meetingUsers = meetingUserService.findMeetingUsers(meetingId);
		return new ApiResponse<List<MeetingUserResponse>>(meetingUsers);
	}
}
