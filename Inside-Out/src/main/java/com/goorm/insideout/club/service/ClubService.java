package com.goorm.insideout.club.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.goorm.insideout.club.dto.ClubInfo;
import com.goorm.insideout.club.dto.requestDto.ClubRequestDto;
import com.goorm.insideout.club.dto.responseDto.ClubListResponseDto;
import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.user.domain.User;

public interface ClubService {

	Club createClub(ClubRequestDto ClubRequestDto, /*MultipartFile multipartFile,*/ User user) throws IOException;

	Club findByClubId(Long ClubId);

	void deleteClub(Long clubId);

	Club modifyClub(ClubRequestDto clubRequestDto, /*MultipartFile multipartFile,*/   User user, Club club) throws
		IOException;

	List<ClubListResponseDto> findAllClubDesc();

	Club ownClub(Long clubId, Long userId);

	Club belongToClub(String selectedClub,Long userId);

	List<ClubListResponseDto> findByCategory(String category);

}
