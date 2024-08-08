package com.goorm.insideout.meeting.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goorm.insideout.auth.dto.CustomUserDetails;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.response.ApiResponse;
import com.goorm.insideout.meeting.domain.Meeting;
import com.goorm.insideout.meeting.dto.response.MeetingApplyResponse;
import com.goorm.insideout.meeting.service.MeetingApplyService;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.userchatroom.service.UserChatRoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/meetings")
@RequiredArgsConstructor
@Tag(name = "MeetingApplyUserController", description = "모임 멤버 신청 관련 API")
public class MeetingApplyController {

	private final MeetingApplyService meetingApplyService;
	private final UserChatRoomService userChatRoomService;

	@PostMapping("/{meetingId}/apply")
	@Operation(summary = "모임 참여 신청 API", description = "모임 참여 신청을 하는 API 입니다.")
	public ApiResponse applyMeetingUser(@PathVariable Long meetingId,
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		meetingApplyService.meetingApply(customUserDetails.getUser(), meetingId);
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	@PostMapping("/apply/{applyId}/accept")
	@Operation(summary = "모임 참여 수락 API ", description = "모임 참여 신청을 수락하는 API 입니다.")
	public ApiResponse acceptApply(@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable("applyId") Long applyId) {
		User host = userDetails.getUser();
		Meeting meeting = meetingApplyService.meetingUserAccept(host, applyId);
		userChatRoomService.inviteUserToChatRoom(meeting.getId(), host);
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	@DeleteMapping("/apply/{applyId}/reject")
	@Operation(summary = "모임 참여 거부 API", description = "모임 참여 신청을 거부하는 API 입니다.")
	public ApiResponse rejectApply(@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable("applyId") Long applyId) {
		User host = userDetails.getUser();
		meetingApplyService.meetingUserReject(host, applyId);
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	@GetMapping("/{meetingId}/apply")
	@Operation(summary = "모임 참여 신청자 조회 API", description = "모임 참여 신청자를 조회하는 API 입니다.")
	public ApiResponse<List<MeetingApplyResponse>> findApplyList(@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable("meetingId") Long meetingId) {
		User owner = userDetails.getUser();
		List<MeetingApplyResponse> applyList = meetingApplyService.findApplyList(meetingId, owner);
		return new ApiResponse<List<MeetingApplyResponse>>(applyList);
	}
}
