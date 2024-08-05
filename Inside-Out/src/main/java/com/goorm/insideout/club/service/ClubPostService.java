package com.goorm.insideout.club.service;

import java.util.List;

import com.goorm.insideout.club.dto.ClubPostDto;
import com.goorm.insideout.club.dto.requestDto.ClubPostRequestDto;
import com.goorm.insideout.club.dto.responseDto.ClubPostListResponseDto;
import com.goorm.insideout.club.dto.responseDto.ClubPostResponseDto;
import com.goorm.insideout.club.entity.ClubPost;
import com.goorm.insideout.user.domain.User;

public interface ClubPostService {
	ClubPost saveClubPost(ClubPostRequestDto clubPostRequestDto, User user);

	ClubPost findByClubPostId(Long clubPostId);

	List<ClubPostListResponseDto> findClubPostByType(Long clubId, String type);

	void deleteClubPost(Long clubPostId);

	ClubPost updateClubPost(ClubPostRequestDto clubRequestPostDto, User user, Long clubPostId);

}
