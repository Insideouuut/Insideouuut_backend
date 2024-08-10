package com.goorm.insideout.club.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goorm.insideout.auth.dto.CustomUserDetails;
import com.goorm.insideout.club.dto.requestDto.ClubApplyRequestDto;
import com.goorm.insideout.club.dto.responseDto.ClubApplyResponseDto;
import com.goorm.insideout.club.dto.responseDto.ClubMembersResponseDto;
import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.club.entity.ClubApply;
import com.goorm.insideout.club.entity.ClubUser;
import com.goorm.insideout.club.service.ClubApplyService;
import com.goorm.insideout.club.service.ClubService;
import com.goorm.insideout.club.service.ClubUserService;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.response.ApiResponse;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.user.service.UserService;
import com.goorm.insideout.userchatroom.service.UserChatRoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs")
@Tag(name = "ClubUserController", description = "동아리 멤버 관련 API")
public class ClubUserController {

	private final ClubService clubService;
	private final ClubUserService clubUserService;
	private final ClubApplyService clubApplyService;
	private final UserChatRoomService userChatRoomService;

	@DeleteMapping("/{clubId}/members")
	@Operation(summary = "동아리 멤버 탈퇴 API", description = "동아리 멤버를 탈퇴하는 API 입니다.")
	public ApiResponse clubUserLeave(
		@PathVariable final Long clubId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		try {
			User user = userDetails.getUser();
			Long userId = user.getId();
			Club club = clubService.findByClubId(clubId);

			if (club == null) {
				return new ApiResponse(ErrorCode.CLUB_NOT_FOUND);
			} else if (clubUserService.clubUserFind(userId, clubId) == null) {
				return new ApiResponse(ErrorCode.CLUB_NOT_MEMBER);
			} else if (club.getOwner().getId().equals(userId)) {
				return new ApiResponse(ErrorCode.INVALID_REQUEST);
			}
			clubUserService.clubUserLeave(club, user);
		} catch (Exception exception) {
			return new ApiResponse(ErrorCode.INVALID_REQUEST);
		}
		return new ApiResponse(ErrorCode.REQUEST_OK);

	}

	@PostMapping("/{clubId}/apply")
	@Operation(summary = "동아리 멤버 지원 API", description = "동아리 멤버로 지원하는 API 입니다.")
	public ApiResponse apply(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("clubId") Long clubId, @Valid @RequestBody ClubApplyRequestDto clubApplyRequestDto) {
		User user = userDetails.getUser();
		Club club = clubService.findByClubId(clubId);
		clubApplyService.clubApply(club, user, clubApplyRequestDto);

		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	@PostMapping("/{clubId}/apply/{applyId}/accept")
	@Operation(summary = "동아리 멤버 지원 승인 API", description = "동아리 멤버 지원을 승인하는 API 입니다.")
	public ApiResponse acceptApply(@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable("clubId") Long clubId, @PathVariable("applyId") Long applyId) {
		User owner = userDetails.getUser();
		Club club = clubService.findByClubId(clubId);
		ClubApply clubApply = clubApplyService.findClubApplyById(applyId);

		clubUserService.clubUserAccept(club, owner, applyId);

		ClubUser clubUser = clubUserService.clubUserFind(clubApply.getUserId(), clubId);

		userChatRoomService.inviteUserToChatRoom(club.getChat_room_id(), clubUser.getUser());

		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	@DeleteMapping("/{clubId}/apply/{applyId}/reject")
	@Operation(summary = "동아리 멤버 지원 거절 API", description = "동아리 멤버 지원을 거절하는 API 입니다.")
	public ApiResponse rejectApply(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("clubId") Long clubId, @PathVariable("applyId") Long applyId) {
		User owner = userDetails.getUser();
		Club club = clubService.findByClubId(clubId);

		clubUserService.clubUserReject(club, owner, applyId);
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	@DeleteMapping("/{clubId}/members/{clubUserId}/expel")
	@Operation(summary = "동아리 멤버 추방 API", description = "동아리 멤버를 추방하는 API 입니다.")
	public ApiResponse memberExpel(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("clubId") Long clubId, @PathVariable("clubUserId") Long clubUserId) {
		User owner = userDetails.getUser();
		Club club = clubService.findByClubId(clubId);

		clubUserService.clubUserExpel(club, owner, clubUserId);
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	@GetMapping("/{clubId}/apply")
	@Operation(summary = "동아리 멤버 지원자 목록 조회 API", description = "동아리 멤버 지원자 목록을 조회하는 API 입니다.")
	public ApiResponse<List<ClubApplyResponseDto>> findApplyList(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("clubId") Long clubId) {
		User owner = userDetails.getUser();
		Club club = clubService.findByClubId(clubId);


		return new ApiResponse<List<ClubApplyResponseDto>>(clubApplyService.findApplyList(club, owner));
	}

	@GetMapping("/{clubId}/members")
	@Operation(summary = "동아리 멤버 목록 조회 API", description = "동아리 멤버 목록을 조회하는 API 입니다.")
	public ApiResponse<List<ClubMembersResponseDto>> findMemberList(@PathVariable("clubId") Long clubId) {
		Club club = clubService.findByClubId(clubId);


		return new ApiResponse<List<ClubMembersResponseDto>>(clubUserService.findMemberList(club));
	}
}
