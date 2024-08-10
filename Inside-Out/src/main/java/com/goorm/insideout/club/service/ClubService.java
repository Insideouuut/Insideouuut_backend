package com.goorm.insideout.club.service;

import java.io.IOException;
import java.util.List;

import com.goorm.insideout.chatroom.domain.ChatRoom;
import com.goorm.insideout.club.dto.requestDto.ClubRequestDto;
import com.goorm.insideout.club.dto.responseDto.ClubBoardResponseDto;
import com.goorm.insideout.club.dto.responseDto.ClubListResponseDto;
import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.user.domain.User;

public interface ClubService {

	Club createClub(ClubRequestDto ClubRequestDto, User user);

	Club findByClubId(Long ClubId);

	void deleteClub(Long clubId);

	Club modifyClub(ClubRequestDto clubRequestDto, User user, Club club);

	List<ClubListResponseDto> findAllClubDesc();

	Club ownClub(Long clubId, Long userId);

	List<ClubListResponseDto> findByCategory(String category);

	public void setChatRoom(Club club, ChatRoom chatRoom);

	public List<ClubListResponseDto> findMyClub(Long userId);

	public List<ClubListResponseDto> findMyApplyClub(Long userId);

	public ClubBoardResponseDto findClubBoard(Long ClubId, User user);
}
