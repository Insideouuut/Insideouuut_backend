package com.goorm.insideout.club.service;

import java.util.List;

import com.goorm.insideout.club.dto.ClubPostDto;
import com.goorm.insideout.club.dto.requestDto.ClubPostRequestDto;
import com.goorm.insideout.club.dto.responseDto.ClubPostListResponseDto;
import com.goorm.insideout.club.entity.ClubPost;
import com.goorm.insideout.user.domain.User;

public interface ClubPostService {
	ClubPost saveClubPost(Long clubId, ClubPostRequestDto clubPostRequestDto, User user);

	ClubPostDto findByClubPostId(Long clubPostId);

	List<ClubPostListResponseDto> findByClubId(Long clubId);

	List<ClubPostListResponseDto> findAll();

	List<ClubPostListResponseDto> findClubPostByType(Long clubId, String type);

	void deleteClubPost(Long clubId, Long clubPostId, User user);

	ClubPost updateClubPost(Long clubId, ClubPostRequestDto clubRequestPostDto, User user, Long clubPostId);

}
