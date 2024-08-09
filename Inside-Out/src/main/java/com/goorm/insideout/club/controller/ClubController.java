package com.goorm.insideout.club.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.goorm.insideout.auth.dto.CustomUserDetails;
import com.goorm.insideout.chatroom.domain.ChatRoom;
import com.goorm.insideout.chatroom.domain.ChatRoomType;
import com.goorm.insideout.chatroom.service.ChatRoomService;
import com.goorm.insideout.club.dto.responseDto.ClubBoardResponseDto;
import com.goorm.insideout.club.dto.responseDto.ClubListResponseDto;
import com.goorm.insideout.club.service.ClubService;
import com.goorm.insideout.club.dto.requestDto.ClubRequestDto;
import com.goorm.insideout.club.dto.responseDto.ClubResponseDto;
import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.response.ApiResponse;
import com.goorm.insideout.image.service.ImageService;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.userchatroom.service.UserChatRoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "ClubController", description = "동아리 관련 API")
public class ClubController {

	private final ClubService clubService;
	private final ChatRoomService chatRoomService;
	private final UserChatRoomService userChatRoomService;
	private final ImageService imageService;

	@GetMapping("/clubs")
	@Operation(summary = "동아리 목록 조회 API", description = "동아리 목록을 조회하는 API 입니다.")
	public ApiResponse<List<ClubListResponseDto>> findByType(@RequestParam(name = "category") String category) {

		return new ApiResponse<List<ClubListResponseDto>>(clubService.findByCategory(category));
	}


	@GetMapping("/clubs/{clubId}")
	@Operation(summary = "동아리 단건 조회 API", description = "동아리를 단건으로 조회하는 API 입니다.")
	public ApiResponse<ClubBoardResponseDto> findClubBoard(@PathVariable Long clubId, @AuthenticationPrincipal CustomUserDetails userDetails) {

		User user = userDetails.getUser();

		return new ApiResponse<>(clubService.findClubBoard(clubId, user));
	}

	@PostMapping("/clubs")
	@Operation(summary = "동아리 생성 API", description = "동아리를 생성하는 API 입니다.")
	public ApiResponse<ClubResponseDto> saveClub(
		@Valid @RequestPart ClubRequestDto clubRequestDto,
		@RequestPart("imageFiles") List<MultipartFile> multipartFiles,
		@AuthenticationPrincipal CustomUserDetails userDetails
	){
		User user;
		Club club;

		try {
			user = userDetails.getUser();
			
			club = clubService.createClub(clubRequestDto, user);
			imageService.saveClubImages(multipartFiles, club.getClubId());

			ChatRoom chatRoom = chatRoomService.createChatRoom(club.getClubId(), club.getClubName(), ChatRoomType.CLUB);
			clubService.setChatRoom(club, chatRoom);
			userChatRoomService.inviteUserToChatRoom(club.getChat_room_id(), user);

		} catch (Exception exception) {
			return new ApiResponse<>(ErrorCode.CLUB_ALREADY_EXIST);
		}
		
		return new ApiResponse<>((ClubResponseDto.of(club.getClubId(), "클럽을 성공적으로 생성하였습니다.")));
	}


	@PutMapping("/clubs/{clubId}")
	@Operation(summary = "동아리 수정 API", description = "동아리를 수정하는 API 입니다.")
	public ApiResponse<ClubResponseDto> updateClub(
		@PathVariable Long clubId,
		@Valid @RequestPart ClubRequestDto clubRequestDto,
		@RequestPart("imageFiles") List<MultipartFile> multipartFiles,
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		User user;
		Club club;

		try {
			user = userDetails.getUser();
			Long userId = user.getId();
			club = clubService.ownClub(clubId, userId);

			if(club==null) {
				return new ApiResponse<>(ErrorCode.CLUB_NOT_FOUND);
			}

			if (!club.getOwner().getEmail().equals(user.getEmail())) {
				return new ApiResponse<>(ErrorCode.CLUB_NOT_OWNER);
			}
			clubService.modifyClub(clubRequestDto, user, club);
			imageService.deleteClubImages(clubId);
			imageService.saveClubImages(multipartFiles, clubId);
		} catch (Exception exception) {
			return new ApiResponse<>(ErrorCode.INVALID_REQUEST);
		}
		return new ApiResponse<>(ClubResponseDto.of(club.getClubId(), "클럽 상세 정보 수정이 완료되었습니다."));

	}

	@DeleteMapping("/clubs/{clubId}")
	@Operation(summary = "동아리 삭제 API", description = "동아리를 삭제하는 API 입니다.")
	public ApiResponse clubDelete(@PathVariable Long clubId, @AuthenticationPrincipal CustomUserDetails userDetails) {
		try {
			User user = userDetails.getUser();
			Long userId = user.getId();
			Club club = clubService.ownClub(clubId, userId);

			if(club==null) {
				return new ApiResponse<>(ErrorCode.CLUB_NOT_FOUND);
			}

			if (!club.getOwner().getEmail().equals(user.getEmail())) {
				return new ApiResponse<>(ErrorCode.CLUB_NOT_OWNER);
			}
			imageService.deleteClubImages(clubId);
			clubService.deleteClub(clubId);
		} catch (Exception exception) {
			return new ApiResponse<>(ErrorCode.INVALID_REQUEST);
		}
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

}
