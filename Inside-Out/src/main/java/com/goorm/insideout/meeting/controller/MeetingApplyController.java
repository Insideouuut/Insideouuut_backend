package com.goorm.insideout.meeting.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goorm.insideout.auth.dto.CustomUserDetails;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.response.ApiResponse;
import com.goorm.insideout.meeting.dto.request.MeetingApplyRequest;
import com.goorm.insideout.meeting.dto.response.MeetingApplyResponse;
import com.goorm.insideout.meeting.dto.response.MeetingQuestionAnswerResponse;
import com.goorm.insideout.meeting.dto.response.MeetingResponse;
import com.goorm.insideout.meeting.service.MeetingApplyService;
import com.goorm.insideout.user.domain.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "MeetingApplyUserController", description = "모임 멤버 신청 관련 API")
public class MeetingApplyController {

	private final MeetingApplyService meetingApplyService;

	@PostMapping("/meetings/{meetingId}/apply")
	@Operation(summary = "모임 참여 신청 API", description = "모임 참여 신청을 하는 API 입니다.")
	public ApiResponse applyMeetingUser(@PathVariable Long meetingId,
		@RequestBody MeetingApplyRequest requestDto,
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		meetingApplyService.meetingApply(customUserDetails.getUser(), meetingId, requestDto.getAnswers());
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	@PostMapping("clubs/meetings/{meetingId}/apply")
	@Operation(summary = "동아리 모임 참여 API", description = "동아리 모임에 참여하는 API 입니다.")
	public ApiResponse applyClubMeetingUser(
		@PathVariable("meetingId") Long meetingId,
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		meetingApplyService.clubMeetingApply(customUserDetails.getUser(), meetingId);
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	@PostMapping("/meetings/apply/{applyId}/accept")
	@Operation(summary = "모임 참여 수락 API ", description = "모임 참여 신청을 수락하는 API 입니다.")
	public ApiResponse acceptApply(@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable("applyId") Long applyId) {
		User host = userDetails.getUser();
		meetingApplyService.meetingUserAccept(host, applyId);
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	@DeleteMapping("/meetings/apply/{applyId}/reject")
	@Operation(summary = "모임 참여 거부 API", description = "모임 참여 신청을 거부하는 API 입니다.")
	public ApiResponse rejectApply(@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable("applyId") Long applyId) {
		User host = userDetails.getUser();
		meetingApplyService.meetingUserReject(host, applyId);
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	@GetMapping("/meetings/{meetingId}/apply")
	@Operation(summary = "모임 참여 신청자 조회 API", description = "모임 참여 신청자를 조회하는 API 입니다.")
	public ApiResponse<List<MeetingApplyResponse>> findApplyList(@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable("meetingId") Long meetingId) {
		User owner = userDetails.getUser();
		List<MeetingApplyResponse> applyList = meetingApplyService.findApplyList(meetingId, owner);
		return new ApiResponse<List<MeetingApplyResponse>>(applyList);
	}

	@GetMapping("/meetings/pending")
	@Operation(summary = "나의 승인대기 모임 목록 조회 API", description = "나의 승인대기 모임 목록을 조회할 수 있는 API 입니다.")
	public ApiResponse<MeetingResponse> findPendingMeetings(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		Pageable pageable
	) {
		Page<MeetingResponse> pendingMeetings = meetingApplyService.findPendingMeetings(customUserDetails.getUser(),
			pageable);

		return new ApiResponse<>(pendingMeetings);
	}

	@GetMapping("/meetings/apply/{applyId}")
	@Operation(summary = "지원자 신청서 보는 API", description = "지원자 신청서 보는 API 입니다.")
	public ApiResponse<List<MeetingQuestionAnswerResponse>> getMeetingAnswers(@PathVariable("applyId") Long applyId) {
		List<MeetingQuestionAnswerResponse> answers = meetingApplyService.getAnswersByApplyId(applyId);
		return new ApiResponse<List<MeetingQuestionAnswerResponse>>(answers);
	}

}
