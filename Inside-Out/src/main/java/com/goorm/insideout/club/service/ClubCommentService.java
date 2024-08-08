package com.goorm.insideout.club.service;

import java.util.List;

import com.goorm.insideout.club.dto.ClubCommentDto;
import com.goorm.insideout.club.dto.requestDto.ClubCommentRequestDto;
import com.goorm.insideout.club.dto.responseDto.ClubCommentListResponseDto;
import com.goorm.insideout.club.entity.ClubComment;
import com.goorm.insideout.user.domain.User;

public interface ClubCommentService {
	ClubComment saveComment(Long clubId, ClubCommentRequestDto clubCommentRequestDto, Long clubPostId, User user);

	List<ClubCommentListResponseDto> findCommentsByClubPostId(Long clubPostId);

	void deleteComment(Long clubId, Long clubCommentId, User user);

	void updateComment(Long clubId, Long clubCommentId,ClubCommentRequestDto clubCommentRequestDto, User user);

	ClubComment findByCommentId(Long clubCommentId);
}
