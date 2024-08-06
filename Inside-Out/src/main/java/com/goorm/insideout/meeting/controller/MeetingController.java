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
import org.springframework.web.multipart.MultipartFile;

import com.goorm.insideout.auth.dto.CustomUserDetails;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.response.ApiResponse;
import com.goorm.insideout.image.service.ImageService;
import com.goorm.insideout.meeting.dto.request.MeetingCreateRequest;
import com.goorm.insideout.meeting.dto.request.MeetingUpdateRequest;
import com.goorm.insideout.meeting.dto.response.MeetingResponse;
import com.goorm.insideout.meeting.service.MeetingService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class MeetingController {
	private final MeetingService meetingService;
	private final ImageService imageService;

	// 모임 생성
	@PostMapping("/meetings")
	public ApiResponse<String> createMeeting(
		@RequestPart MeetingCreateRequest request,
		@RequestPart("meetingImageFiles") List<MultipartFile> multipartFiles,
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		meetingService.save(request, customUserDetails);

		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	// 모임 단건 조회
	@GetMapping("/meetings/{meetingId}")
	public ApiResponse<MeetingResponse> findById(@PathVariable Long meetingId) {
		MeetingResponse meetingResponse = meetingService.findById(meetingId);
		meetingResponse.addImages(imageService.findMeetingImages(meetingId));

		return new ApiResponse<>(meetingResponse);
	}

	// 나의 승인대기 모임 목록 조회
	@GetMapping("/meetings/pending")
	public ApiResponse<MeetingResponse> findPendingMeetings(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		Pageable pageable
	) {
		Page<MeetingResponse> pendingMeetings = meetingService.findPendingMeetings(customUserDetails, pageable);

		return new ApiResponse<>(pendingMeetings);
	}

	// 나의 참여중인 모임 목록 조회
	@GetMapping("/meetings/participating")
	public ApiResponse<MeetingResponse> findParticipatingMeetings(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		Pageable pageable
	) {
		Page<MeetingResponse> pendingMeetings = meetingService.findParticipatingMeetings(customUserDetails, pageable);

		return new ApiResponse<>(pendingMeetings);
	}

	// 나의 종료된 모임 목록 조회
	@GetMapping("/meetings/ended")
	public ApiResponse<MeetingResponse> findClosedMeetings(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		Pageable pageable
	) {
		Page<MeetingResponse> pendingMeetings = meetingService.findEndedMeetings(customUserDetails, pageable);

		return new ApiResponse<>(pendingMeetings);
	}

	// 나의 운영중인 모임 목록 조회
	@GetMapping("/meetings/running")
	public ApiResponse<MeetingResponse> findRunningMeetings(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		Pageable pageable
	) {
		Page<MeetingResponse> pendingMeetings = meetingService.findRunningMeetings(customUserDetails, pageable);

		return new ApiResponse<>(pendingMeetings);
	}

	// 모임 정보 수정
	@PatchMapping("/meetings/{meetingId}")
	public ApiResponse updateMeeting(
		@PathVariable Long meetingId,
		@RequestBody MeetingUpdateRequest request,
		@RequestPart(value = "meetingImageFiles", required = false) List<MultipartFile> multipartFiles,
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		meetingService.updateById(customUserDetails, meetingId, request);
		imageService.deleteMeetingImages(meetingId);
		imageService.saveMeetingImages(multipartFiles, meetingId);

		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	// 모임 삭제
	@DeleteMapping("/meetings/{meetingId}")
	public ApiResponse deleteMeeting(
		@PathVariable Long meetingId,
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		meetingService.deleteById(customUserDetails, meetingId);

		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	// 모임 종료
	@PatchMapping("/meetings/{meetingId}/end")
	public ApiResponse endMeeting(
		@PathVariable Long meetingId,
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		meetingService.endMeeting(customUserDetails, meetingId);

		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}
}
