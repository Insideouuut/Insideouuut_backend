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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
import com.goorm.insideout.club.repository.ClubRepository;
import com.goorm.insideout.club.service.ClubPostService;
import com.goorm.insideout.club.service.ClubUserService;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.response.ApiResponse;
import com.goorm.insideout.image.service.ImageService;
import com.goorm.insideout.user.domain.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs")
@Tag(name = "ClubPostController", description = "동아리 게시판 관련 API")
public class ClubPostController {

	private final ClubPostService clubPostService;
	private final ClubUserService clubUserService;
	private final ClubRepository clubRepository;
	private final ImageService imageService;

	@GetMapping("/{clubId}/post")
	@Operation(summary = "동아리 게시글 목록 조회 API", description = "동아리 게시글 목록을 조회하는 API 입니다.")
	public ApiResponse<List<ClubPostListResponseDto>> findByPostType(@PathVariable Long clubId,
		@RequestParam(name = "category") String category) {

		return new ApiResponse<List<ClubPostListResponseDto>>(clubPostService.findClubPostByType(clubId, category));
	}

	@GetMapping("/{clubId}/post/{postId}")
	@Operation(summary = "동아리 게시글 단건 조회 API", description = "동아리 게시글을 조회하는 API 입니다.")
	public ApiResponse<ClubPostDto> findClubPost(@PathVariable Long postId) {

		return new ApiResponse<>(ClubPostDto.of(clubPostService.findByClubPostId(postId)));
	}

	@PostMapping("/{clubId}/post")
	@Operation(summary = "동아리 게시글 생성 API", description = "동아리 게시글을 생성하는 API 입니다.")
	public ApiResponse<ClubPostResponseDto> saveClubPost(
		@PathVariable Long clubId,
		@RequestPart("request") ClubPostRequestDto clubPostRequestDto,
		@RequestPart("imageFiles") List<MultipartFile> multipartFiles,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {

		User user = userDetails.getUser();
		ClubPost clubPost = clubPostService.saveClubPost(clubId, clubPostRequestDto, user);
		imageService.saveClubPostImages(multipartFiles, clubPost.getId());

		return new ApiResponse<>((ClubPostResponseDto.of(clubPost.getId(), "게시글을 성공적으로 생성하였습니다.")));
	}

	@PutMapping("/{clubId}/post/{postId}")
	@Operation(summary = "동아리 게시글 수정 API", description = "동아리 게시글을 수정하는 API 입니다.")
	public ApiResponse<ClubPostResponseDto> updateClubPost(
		@PathVariable Long clubId,
		@PathVariable Long postId,
		@RequestPart("request") ClubPostRequestDto clubPostRequestDto,
		@RequestPart("imageFiles") List<MultipartFile> multipartFiles,
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		User user = userDetails.getUser();

		clubPostService.updateClubPost(clubId, clubPostRequestDto, user, postId);
		imageService.deleteClubPostImages(postId);
		imageService.saveClubPostImages(multipartFiles, postId);

		return new ApiResponse<>(ClubPostResponseDto.of(postId, "게시글 상세 정보 수정이 완료되었습니다."));

	}

	@DeleteMapping("/{clubId}/post/{postId}")
	@Operation(summary = "동아리 게시글 삭제 API", description = "동아리 게시글을 삭제하는 API 입니다.")
	public ApiResponse deleteClubPost(@PathVariable Long clubId, @PathVariable Long postId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		User user = userDetails.getUser();

		clubPostService.deleteClubPost(clubId, postId, user);
		imageService.deleteClubPostImages(postId);

		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}
}
