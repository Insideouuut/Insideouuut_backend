package com.goorm.insideout.club.service;

import java.awt.print.Pageable;
import java.io.IOException;
import java.util.List;

import org.hibernate.query.Page;

import com.goorm.insideout.club.dto.ClubPostDto;
import com.goorm.insideout.club.dto.requestDto.ClubRequestDto;
import com.goorm.insideout.club.dto.responseDto.ClubListResponseDto;
import com.goorm.insideout.club.dto.responseDto.ClubPostListResponseDto;
import com.goorm.insideout.club.dto.responseDto.ClubPostResponseDto;
import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.club.entity.ClubPost;
import com.goorm.insideout.user.domain.User;

public interface ClubPostService {
	ClubPost save(ClubPostDto boardDto);

	ClubPost findByBoardId(Long boardId);

	List<ClubPostListResponseDto> findAllClubPost(ClubPost clubPost);

	boolean deleteClubPost(ClubPostDto clubPostDto);

	ClubPost updateClubPost(ClubPostDto clubPostDto);

	ClubPostResponseDto findClubPost(Long clubPostId);
	int board_like(ClubPostDto clubPostDto);

	Integer passwordVerify(ClubPostDto clubPostDto);
}
