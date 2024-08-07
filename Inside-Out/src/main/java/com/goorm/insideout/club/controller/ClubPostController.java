package com.goorm.insideout.club.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.goorm.insideout.auth.dto.CustomUserDetails;
import com.goorm.insideout.chatroom.domain.ChatRoom;
import com.goorm.insideout.chatroom.domain.ChatRoomType;
import com.goorm.insideout.club.dto.ClubPostDto;
import com.goorm.insideout.club.dto.requestDto.ClubPostRequestDto;
import com.goorm.insideout.club.dto.requestDto.ClubRequestDto;
import com.goorm.insideout.club.dto.responseDto.ClubBoardResponseDto;
import com.goorm.insideout.club.dto.responseDto.ClubListResponseDto;
import com.goorm.insideout.club.dto.responseDto.ClubPostListResponseDto;
import com.goorm.insideout.club.dto.responseDto.ClubPostResponseDto;
import com.goorm.insideout.club.dto.responseDto.ClubResponseDto;
import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.club.entity.ClubPost;
import com.goorm.insideout.club.service.ClubPostService;
import com.goorm.insideout.club.service.ClubUserService;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.response.ApiResponse;
import com.goorm.insideout.user.domain.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs")
public class ClubPostController {

	private final ClubPostService clubPostService;
	private final ClubUserService clubUserService;

	@GetMapping("/{clubId}/post")
	public ApiResponse<List<ClubPostListResponseDto>> findByPostType(@PathVariable Long clubId, @RequestParam(name = "category") String category) {

		return new ApiResponse<List<ClubPostListResponseDto>>(clubPostService.findClubPostByType(clubId, category));
	}




	@GetMapping("/{clubId}/post/{postId}")
	public ApiResponse<ClubPostDto> findClubPost(@PathVariable Long postId) {


		return new ApiResponse<ClubPostDto>(ClubPostDto.of(clubPostService.findByClubPostId(postId)));
	}

	@PostMapping("/{clubId}/post")
	public ApiResponse<ClubPostResponseDto> saveClubPost(@Valid @RequestBody ClubPostRequestDto clubPostRequestDto, @AuthenticationPrincipal CustomUserDetails userDetails){

		User user = userDetails.getUser();
		ClubPost clubPost;

		try {
			clubPost = clubPostService.saveClubPost(clubPostRequestDto, user);

		} catch (Exception exception) {
			return new ApiResponse<>(ErrorCode.INVALID_REQUEST);
		}
		return new ApiResponse<ClubPostResponseDto>((ClubPostResponseDto.of(clubPost.getId(), "게시글을 성공적으로 생성하였습니다.")));
	}


	@PutMapping("/{clubId}/post/{postId}")
	public ApiResponse<ClubPostResponseDto> updateClubPost(@PathVariable Long clubId, @PathVariable Long postId, @Valid @RequestBody ClubPostRequestDto clubPostRequestDto, @AuthenticationPrincipal CustomUserDetails userDetails) {

		User user = userDetails.getUser();
		ClubPost clubPost;

		/*
		try {
			clubPost = clubPostService.findByClubPostId(postId);

			if(clubPost==null) {
				return new ApiResponse<>(ErrorCode.INVALID_REQUEST);
			}

			clubPostService.updateClubPost(clubPostRequestDto, user, postId);
		} catch (Exception exception) {
			return new ApiResponse<>(ErrorCode.INVALID_REQUEST);
		}

		 */

		clubPostService.updateClubPost(clubPostRequestDto, user, postId);

		return new ApiResponse<ClubPostResponseDto>(ClubPostResponseDto.of(postId, "게시글 상세 정보 수정이 완료되었습니다."));

	}

	@DeleteMapping("/{clubId}/post/{postId}")
	public ApiResponse deleteClubPost(@PathVariable Long clubId, @PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails userDetails) {
		try {
			User user = userDetails.getUser();
			ClubPost clubPost = clubPostService.findByClubPostId(postId);

			/*
			if(clubPost==null) {
				return new ApiResponse<>(ErrorCode.CLUB_NOT_FOUND);
			}

			if (!clubPost.getClubUser().getClubUserId().equals(clubUserService.clubUserFind(user.getId(), clubId).getClubUserId())) {
				return new ApiResponse<>(ErrorCode.USER_NOT_AUTHENTICATED);
			}

			 */
			clubPostService.deleteClubPost(postId, user);
		} catch (Exception exception) {
			return new ApiResponse<>(ErrorCode.INVALID_REQUEST);
		}
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

}
