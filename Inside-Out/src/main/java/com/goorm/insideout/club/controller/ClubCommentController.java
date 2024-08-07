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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs")
public class ClubCommentController {

	private final ClubCommentService clubCommentService;

	@GetMapping("/{clubId}/post/{postId}/comment")
	public ApiResponse<List<ClubCommentListResponseDto>> findByClubPostId(@PathVariable Long postId) {
		return new ApiResponse<List<ClubCommentListResponseDto>>(clubCommentService.findCommentsByClubPostId(postId));
	}

	@PostMapping("/{clubId}/post/{postId}/comment")
	public ApiResponse<ClubCommentResponseDto> saveClubComment(@PathVariable Long postId, @Valid @RequestBody ClubCommentRequestDto clubCommentRequestDto, @AuthenticationPrincipal CustomUserDetails userDetails){

		User user = userDetails.getUser();
		ClubComment clubComment;

		clubComment = clubCommentService.saveComment(clubCommentRequestDto, postId, user);

		return new ApiResponse<ClubCommentResponseDto>((ClubCommentResponseDto.of(clubComment.getId(), "댓글을 성공적으로 생성하였습니다.")));
	}


	@PutMapping("/{clubId}/post/{postId}/comment/{commentId}")
	public ApiResponse<ClubCommentResponseDto> updateClubComment(@PathVariable Long postId, @PathVariable Long commentId, @Valid @RequestBody ClubCommentRequestDto clubCommentRequestDto, @AuthenticationPrincipal CustomUserDetails userDetails) {

		User user;

		user = userDetails.getUser();

		clubCommentService.updateComment(commentId, clubCommentRequestDto, user);

		return new ApiResponse<ClubCommentResponseDto>(ClubCommentResponseDto.of(postId, "댓글 수정이 완료되었습니다."));

	}

	@DeleteMapping("/{clubId}/post/{postId}/comment/{commentId}")
	public ApiResponse deleteClubComment(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal CustomUserDetails userDetails) {

		User user = userDetails.getUser();

		clubCommentService.deleteComment(commentId, user);

		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}
}
