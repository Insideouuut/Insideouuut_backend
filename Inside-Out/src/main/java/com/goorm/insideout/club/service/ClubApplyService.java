package com.goorm.insideout.club.service;

import java.util.List;

import com.goorm.insideout.club.dto.requestDto.ClubApplyRequestDto;
import com.goorm.insideout.club.dto.responseDto.ClubApplyResponseDto;
import com.goorm.insideout.club.dto.responseDto.ClubMembersResponseDto;
import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.club.entity.ClubApply;
import com.goorm.insideout.club.entity.ClubUser;
import com.goorm.insideout.user.domain.User;

public interface ClubApplyService {
	// 팀에  속해있는지 검색
	ClubApply clubApplyFind(Long userId, Long clubId);

	void clubApplyDelete(Long userId, Long clubId);

	ClubApply clubApply(Club club, User user, ClubApplyRequestDto clubApplyRequestDto);

	List<ClubApplyResponseDto> findApplyList(Club club, User user);
}
