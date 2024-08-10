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

	Club createClub(ClubRequestDto ClubRequestDto, User user);

	Club findByClubId(Long ClubId);

	ClubBoardResponseDto findClubBoard(Long clubId);

	void deleteClub(Long clubId);

	Club modifyClub(ClubRequestDto clubRequestDto, User user, Club club);

	List<ClubBoardResponseDto> findAllClubDesc();

	Club ownClub(Long clubId, Long userId);
	// 정렬 타입에 따른 조회
	List<ClubBoardResponseDto> findBySortType(SearchRequest condition);

	// 검색 조건 및 정렬 타입에 따른 조회
	List<ClubBoardResponseDto> findByConditionAndSortType(SearchRequest condition);

	List<ClubListResponseDto> findByCategory(String category);

	public void setChatRoom(Club club, ChatRoom chatRoom);

	public List<ClubListResponseDto> findMyClub(Long userId);

	public List<ClubListResponseDto> findMyApplyClub(Long userId);
}
