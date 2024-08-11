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
import org.springframework.web.bind.annotation.RestController;

import com.goorm.insideout.auth.dto.CustomUserDetails;
import com.goorm.insideout.club.dto.requestDto.ClubCommentRequestDto;
import com.goorm.insideout.club.dto.requestDto.ClubPostRequestDto;
import com.goorm.insideout.club.dto.responseDto.ClubCommentListResponseDto;
import com.goorm.insideout.club.dto.responseDto.ClubCommentResponseDto;
import com.goorm.insideout.club.dto.responseDto.ClubPostResponseDto;
import com.goorm.insideout.club.entity.ClubComment;
import com.goorm.insideout.club.entity.ClubPost;
import com.goorm.insideout.club.service.ClubCommentService;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.response.ApiResponse;
import com.goorm.insideout.user.domain.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs")
@Tag(name = "ClubCommentController", description = "동아리 게시판 댓글 관련 API")
public class ClubCommentController {

	private final ClubCommentService clubCommentService;

	@GetMapping("/{clubId}/posts/{postId}/comment")
	@Operation(summary = "동아리 게시글 댓글 조회 API", description = "동아리 게시글의 댓글을 조회하는 API 입니다.")
	public ApiResponse<List<ClubCommentListResponseDto>> findByClubPostId(@PathVariable Long postId) {
		return new ApiResponse<List<ClubCommentListResponseDto>>(clubCommentService.findCommentsByClubPostId(postId));
	}

	@PostMapping("/{clubId}/posts/{postId}/comment")
	@Operation(summary = "동아리 게시글 댓글 생성 API", description = "동아리 게시글의 댓글을 생성하는 API 입니다.")
	public ApiResponse<ClubCommentResponseDto> saveClubComment(@PathVariable Long clubId, @PathVariable Long postId, @Valid @RequestBody ClubCommentRequestDto clubCommentRequestDto, @AuthenticationPrincipal CustomUserDetails userDetails){

		User user = userDetails.getUser();
		ClubComment clubComment;

		clubComment = clubCommentService.saveComment(clubId, clubCommentRequestDto, postId, user);

		return new ApiResponse<ClubCommentResponseDto>((ClubCommentResponseDto.of(clubComment.getId(), "댓글을 성공적으로 생성하였습니다.")));
	}


	@PutMapping("/{clubId}/posts/{postId}/comment/{commentId}")
	@Operation(summary = "동아리 게시글 댓글 수정 API", description = "동아리 게시글의 댓글을 수정하는 API 입니다.")
	public ApiResponse<ClubCommentResponseDto> updateClubComment(@PathVariable Long clubId, @PathVariable Long postId, @PathVariable Long commentId, @Valid @RequestBody ClubCommentRequestDto clubCommentRequestDto, @AuthenticationPrincipal CustomUserDetails userDetails) {

		User user;

		user = userDetails.getUser();

		clubCommentService.updateComment(clubId, commentId, clubCommentRequestDto, user);

		return new ApiResponse<ClubCommentResponseDto>(ClubCommentResponseDto.of(postId, "댓글 수정이 완료되었습니다."));

	}

	@DeleteMapping("/{clubId}/posts/{postId}/comment/{commentId}")
	@Operation(summary = "동아리 게시글 댓글 삭제 API", description = "동아리 게시글의 댓글을 삭제하는 API 입니다.")
	public ApiResponse deleteClubComment(@PathVariable Long clubId, @PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal CustomUserDetails userDetails) {

		User user = userDetails.getUser();

		clubCommentService.deleteComment(clubId, commentId, user);

		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}
}
