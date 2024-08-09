package com.goorm.insideout.meeting.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.goorm.insideout.chatroom.domain.ChatRoom;
import com.goorm.insideout.chatroom.domain.ChatRoomType;
import com.goorm.insideout.chatroom.service.ChatRoomService;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.response.ApiResponse;
import com.goorm.insideout.image.service.ImageService;
import com.goorm.insideout.meeting.domain.Meeting;
import com.goorm.insideout.meeting.dto.request.MeetingCreateRequest;
import com.goorm.insideout.meeting.dto.request.MeetingUpdateRequest;
import com.goorm.insideout.meeting.dto.response.MeetingResponse;
import com.goorm.insideout.meeting.service.MeetingService;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.userchatroom.service.UserChatRoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "MeetingController", description = "모임 관련 API")
public class MeetingController {
	private final MeetingService meetingService;
	private final ImageService imageService;
	private final ChatRoomService chatRoomService;
	private final UserChatRoomService userChatRoomService;

	@PostMapping("/meetings")
	@Operation(summary = "모임 생성 API", description = "모임을 생성하는 API 입니다. 아직 이미지 업로드 기능이 준비되지 않았기 때문에, meetingImage 필드는 제외하고 요청 보내주시면 됩니다.")
	public ApiResponse<String> createMeeting(
		@RequestPart("request") MeetingCreateRequest request,
		@RequestPart("imageFiles") List<MultipartFile> multipartFiles,
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		User user = customUserDetails.getUser();
		Meeting meeting = meetingService.save(request, user);
		imageService.saveMeetingImages(multipartFiles, meeting.getId());

		ChatRoom chatRoom = chatRoomService.createChatRoom(meeting.getId(), meeting.getTitle(), ChatRoomType.MEETING);
		Meeting saveMeeting = meetingService.injectMeetingChatRoom(meeting.getId(), chatRoom);
		userChatRoomService.inviteUserToChatRoom(saveMeeting.getChatRoom().getId(), user);

		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	@GetMapping("/meetings")
	@Operation(summary = "모임 전체 조회 API", description = "모임 전체를 조회할 수 있는 API 입니다.")
	public ApiResponse<MeetingResponse> findAll() {
		return new ApiResponse<>(meetingService.findAll());
	}

	@GetMapping("/meetings/{meetingId}")
	@Operation(summary = "모임 단건 조회 API", description = "모임을 단건으로 조회할 수 있는 API 입니다.")
	public ApiResponse<MeetingResponse> findById(@PathVariable Long meetingId) {
		return new ApiResponse<>(meetingService.findById(meetingId));
	}

	@GetMapping("/meetings/participating")
	@Operation(summary = "나의 참여중인 모임 목록 조회 API", description = "나의 참여중인 모임 목록을 조회할 수 있는 API 입니다.")
	public ApiResponse<MeetingResponse> findParticipatingMeetings(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		Pageable pageable
	) {
		Page<MeetingResponse> pendingMeetings = meetingService.findParticipatingMeetings(customUserDetails.getUser(),
			pageable);

		return new ApiResponse<>(pendingMeetings);
	}

	@GetMapping("/meetings/ended")
	@Operation(summary = "나의 종료된 모임 목록 조회 API", description = "나의 종료된 모임 목록을 조회할 수 있는 API 입니다.")
	public ApiResponse<MeetingResponse> findClosedMeetings(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		Pageable pageable
	) {
		Page<MeetingResponse> pendingMeetings = meetingService.findEndedMeetings(customUserDetails.getUser(), pageable);

		return new ApiResponse<>(pendingMeetings);
	}

	@GetMapping("/meetings/running")
	@Operation(summary = "나의 운영중인 모임 목록 조회 API", description = "나의 운영중인 모임 목록을 조회할 수 있는 API 입니다.")
	public ApiResponse<MeetingResponse> findRunningMeetings(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		Pageable pageable
	) {
		Page<MeetingResponse> pendingMeetings = meetingService.findRunningMeetings(customUserDetails.getUser(),
			pageable);

		return new ApiResponse<>(pendingMeetings);
	}

	@PatchMapping("/meetings/{meetingId}")
	@Operation(summary = "모임 정보 수정 API", description = "모임 정보를 수정할 수 있는 API 입니다. 아직 이미지 업로드 기능이 준비되지 않았기 때문에, meetingImage 필드는 제외하고 요청 보내주시면 됩니다.")
	public ApiResponse updateMeeting(
		@PathVariable Long meetingId,
		@RequestBody MeetingUpdateRequest request,
		@RequestPart(value = "imageFiles", required = false) List<MultipartFile> multipartFiles,
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		meetingService.updateById(customUserDetails.getUser(), meetingId, request);
		imageService.deleteMeetingImages(meetingId);
		imageService.saveMeetingImages(multipartFiles, meetingId);

		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	@DeleteMapping("/meetings/{meetingId}")
	@Operation(summary = "모임 삭제 API", description = "모임을 삭제할 수 있는 API 입니다.")
	public ApiResponse deleteMeeting(
		@PathVariable Long meetingId,
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		imageService.deleteMeetingImages(meetingId);
		meetingService.deleteById(customUserDetails.getUser(), meetingId);

		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	@PatchMapping("/meetings/{meetingId}/end")
	@Operation(summary = "모임 종료 API", description = "모임을 종료할 수 있는 API 입니다. 모임 삭제와는 달리 단순히 모임을 종료시키는 기능입니다.")
	public ApiResponse endMeeting(
		@PathVariable Long meetingId,
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		meetingService.endMeeting(customUserDetails.getUser(), meetingId);

		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}
}
