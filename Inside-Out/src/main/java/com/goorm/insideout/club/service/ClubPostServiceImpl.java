package com.goorm.insideout.club.service;

import java.util.List;

import com.goorm.insideout.club.dto.ClubPostDto;
import com.goorm.insideout.club.dto.responseDto.ClubPostListResponseDto;
import com.goorm.insideout.club.dto.responseDto.ClubPostResponseDto;
import com.goorm.insideout.club.entity.ClubPost;

public class ClubPostServiceImpl implements ClubPostService{
	@Override
	public ClubPost save(ClubPostDto clubPostDto) {
		return null;
	}

	@Override
	public ClubPost findByClubPostId(Long clubPostId) {
		return null;
	}

	@Override
	public List<ClubPostListResponseDto> findAllClubPost(ClubPost clubPost) {
		return null;
	}

	@Override
	public boolean deleteClubPost(ClubPostDto clubPostDto) {
		return false;
	}

	@Override
	public ClubPost updateClubPost(ClubPostDto clubPostDto) {
		return null;
	}

	@Override
	public ClubPostResponseDto findClubPost(Long clubPostId) {
		return null;
	}
	
	@Override
	public Integer passwordVerify(ClubPostDto clubPostDto) {
		return null;
	}
}
