package com.goorm.insideout.meeting.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.goorm.insideout.auth.dto.CustomUserDetails;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.response.ApiResponse;
import com.goorm.insideout.meeting.dto.request.MeetingCreateRequest;
import com.goorm.insideout.meeting.dto.request.MeetingUpdateRequest;
import com.goorm.insideout.meeting.dto.response.MeetingResponse;
import com.goorm.insideout.meeting.service.MeetingService;
import com.goorm.insideout.user.domain.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "MeetingController", description = "모임 관련 API")
public class MeetingController {
	private final MeetingService meetingService;


	// 모임 생성
	@PostMapping("/meetings")
	@Operation(summary = "모임 생성 API", description = "모임을 생성하는 API 입니다. 아직 이미지 업로드 기능이 준비되지 않았기 때문에, meetingImage 필드는 제외하고 요청 보내주시면 됩니다.")
	public ApiResponse<String> createMeeting(
		@RequestBody MeetingCreateRequest request,
		@RequestPart(value = "meetingImage", required = false) List<MultipartFile> multipartFiles,
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		User user = customUserDetails.getUser();
		meetingService.save(request, user);

		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	@GetMapping("/meetings")
	@Operation(summary = "모임 전체 조회 API", description = "모임 전체를 조회할 수 있는 API 입니다.")
	public ApiResponse<MeetingResponse> findAll() {
		return new ApiResponse<>(meetingService.findAll());
	}

	// 모임 단건 조회
	@GetMapping("/meetings/{meetingId}")
	@Operation(summary = "모임 단건 조회 API", description = "모임을 단건으로 조회할 수 있는 API 입니다.")
	public ApiResponse<MeetingResponse> findById(@PathVariable Long meetingId) {
		MeetingResponse meetingResponse = meetingService.findById(meetingId);

		return new ApiResponse<>(meetingResponse);
	}

	// 나의 승인대기 모임 목록 조회
	@GetMapping("/meetings/pending")
	@Operation(summary = "나의 승인대기 모임 목록 조회 API", description = "나의 승인대기 모임 목록을 조회할 수 있는 API 입니다.")
	public ApiResponse<MeetingResponse> findPendingMeetings(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		Pageable pageable
	) {
		Page<MeetingResponse> pendingMeetings = meetingService.findPendingMeetings(customUserDetails, pageable);

		return new ApiResponse<>(pendingMeetings);
	}

	// 나의 참여중인 모임 목록 조회
	@GetMapping("/meetings/participating")
	@Operation(summary = "나의 참여중인 모임 목록 조회 API", description = "나의 참여중인 모임 목록을 조회할 수 있는 API 입니다.")
	public ApiResponse<MeetingResponse> findParticipatingMeetings(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		Pageable pageable
	) {
		Page<MeetingResponse> pendingMeetings = meetingService.findParticipatingMeetings(customUserDetails, pageable);

		return new ApiResponse<>(pendingMeetings);
	}

	// 나의 종료된 모임 목록 조회
	@GetMapping("/meetings/ended")
	@Operation(summary = "나의 종료된 모임 목록 조회 API", description = "나의 종료된 모임 목록을 조회할 수 있는 API 입니다.")
	public ApiResponse<MeetingResponse> findClosedMeetings(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		Pageable pageable
	) {
		Page<MeetingResponse> pendingMeetings = meetingService.findEndedMeetings(customUserDetails, pageable);

		return new ApiResponse<>(pendingMeetings);
	}

	// 나의 운영중인 모임 목록 조회
	@GetMapping("/meetings/running")
	@Operation(summary = "나의 운영중인 모임 목록 조회 API", description = "나의 운영중인 모임 목록을 조회할 수 있는 API 입니다.")
	public ApiResponse<MeetingResponse> findRunningMeetings(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		Pageable pageable
	) {
		Page<MeetingResponse> pendingMeetings = meetingService.findRunningMeetings(customUserDetails, pageable);

		return new ApiResponse<>(pendingMeetings);
	}

	// 모임 정보 수정
	@PatchMapping("/meetings/{meetingId}")
	@Operation(summary = "모임 정보 수정 API", description = "모임 정보를 수정할 수 있는 API 입니다. 아직 이미지 업로드 기능이 준비되지 않았기 때문에, meetingImage 필드는 제외하고 요청 보내주시면 됩니다.")
	public ApiResponse updateMeeting(
		@PathVariable Long meetingId,
		@RequestBody MeetingUpdateRequest request,
		@RequestPart(value = "meetingImage", required = false) List<MultipartFile> multipartFiles,
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		meetingService.updateById(customUserDetails, meetingId, request);

		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	// 모임 삭제
	@DeleteMapping("/meetings/{meetingId}")
	@Operation(summary = "모임 삭제 API", description = "모임을 삭제할 수 있는 API 입니다.")
	public ApiResponse deleteMeeting(
		@PathVariable Long meetingId,
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		meetingService.deleteById(customUserDetails, meetingId);

		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	// 모임 종료
	@PatchMapping("/meetings/{meetingId}/end")
	@Operation(summary = "모임 종료 API", description = "모임을 종료할 수 있는 API 입니다. 모임 삭제와는 달리 단순히 모임을 종료시키는 기능입니다.")
	public ApiResponse endMeeting(
		@PathVariable Long meetingId,
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		meetingService.endMeeting(customUserDetails, meetingId);

		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}
}
