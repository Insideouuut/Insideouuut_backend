package com.goorm.insideout.club.service;

import java.io.IOException;
import java.util.List;

import com.goorm.insideout.chatroom.domain.ChatRoom;
import com.goorm.insideout.club.dto.requestDto.ClubRequestDto;
import com.goorm.insideout.club.dto.responseDto.ClubBoardResponseDto;
import com.goorm.insideout.club.dto.responseDto.ClubListResponseDto;
import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.meeting.dto.request.SearchRequest;
import com.goorm.insideout.user.domain.User;

public interface ClubService {

	Club createClub(ClubRequestDto ClubRequestDto, /*MultipartFile multipartFile,*/ User user) throws IOException;

	Club findByClubId(Long ClubId);

	void deleteClub(Long clubId);

	Club modifyClub(ClubRequestDto clubRequestDto, /*MultipartFile multipartFile,*/   User user, Club club) throws
		IOException;

	List<ClubListResponseDto> findAllClubDesc();

	Club ownClub(Long clubId, Long userId);

	// 정렬 타입에 따른 조회
	List<ClubBoardResponseDto> findBySortType(SearchRequest condition);

	// 검색 조건 및 정렬 타입에 따른 조회
	List<ClubBoardResponseDto> findByConditionAndSortType(SearchRequest condition);

	Club belongToClub(Long userId);

	List<ClubListResponseDto> findByCategory(String category);

	public void setChatRoom(Club club, ChatRoom chatRoom);
}
