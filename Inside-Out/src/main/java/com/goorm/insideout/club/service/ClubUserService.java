package com.goorm.insideout.club.service;

import java.util.List;

import com.goorm.insideout.auth.dto.CustomUserDetails;
import com.goorm.insideout.club.dto.responseDto.ClubMembersResponseDto;
import com.goorm.insideout.club.dto.responseDto.ClubUserAuthorityResponse;
import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.club.entity.ClubUser;
import com.goorm.insideout.user.domain.User;

public interface ClubUserService {

	// 팀에  속해있는지 검색
	ClubUser clubUserFind(Long userId, Long clubId);

	void clubUserDelete(Long userId, Long clubId);

	ClubUser clubUserAccept(Club club, User user, Long applyId);

	void clubUserReject(Club club, User user, Long applyId);

	//List<ClubApplyResponseDto> findApplyList(Club club, User user);

	ClubUserAuthorityResponse checkUserAuthority(Long clubId, CustomUserDetails customUserDetails);

	//List<ClubDetailResponseDto> findMemberList(Club club);
	List<ClubMembersResponseDto> findMemberList(Club club);

	void clubUserLeave(Club club, User user);

	void clubUserExpel(Club club, User user, Long applyId);

	ClubUser clubUserReport(Club club, User user);
}
