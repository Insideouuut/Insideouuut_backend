package com.goorm.insideout.club.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.insideout.auth.dto.CustomUserDetails;
import com.goorm.insideout.club.dto.responseDto.ClubMembersResponseDto;
import com.goorm.insideout.club.dto.responseDto.ClubUserAuthorityResponse;
import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.club.entity.ClubApply;
import com.goorm.insideout.club.repository.ClubApplyRepository;
import com.goorm.insideout.club.repository.ClubRepository;
import com.goorm.insideout.club.repository.ClubUserRepository;
import com.goorm.insideout.club.entity.ClubUser;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.image.domain.ProfileImage;
import com.goorm.insideout.image.repository.ProfileImageRepository;
import com.goorm.insideout.meeting.domain.MeetingUser;
import com.goorm.insideout.meeting.domain.Role;
import com.goorm.insideout.meeting.dto.response.MeetingUserAuthorityResponse;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class ClubUserServiceImpl implements ClubUserService {

	private final ClubUserRepository clubUserRepository;
	private final ClubRepository clubRepository;
	private final ClubApplyRepository clubApplyRepository;
	private final ProfileImageRepository profileImageRepository;

	@Override
	public ClubUser clubUserFind(Long userId, Long clubId) {

		return clubUserRepository.findByUserIdAndClubId(userId, clubId)
			.orElseThrow(() -> ModongException.from(ErrorCode.CLUB_NOT_AUTHORIZED));
	}

	@Modifying(clearAutomatically = true)
	@Transactional
	@Override
	public void clubUserDelete(Long userId, Long clubId) {
		clubUserRepository.deleteByUserIdAndClubId(userId, clubId);
	}

	@Override
	@Transactional
	public ClubUser clubUserAccept(Club club, User user, Long applyId) {
		ClubApply clubApply = clubApplyRepository.findByApplyId(applyId).orElseThrow(() -> ModongException.from(
			ErrorCode.USER_NOT_FOUND));
		Integer memberLimit = club.getMemberLimit();
		Integer memberCount = club.getMemberCount();

		if (!isOwner(club, user)) {
			throw new IllegalStateException();
		}
		if (memberCount >= memberLimit) {
			throw new NoSuchElementException();
		}

		ProfileImage profileImage = profileImageRepository.findByUserId(user.getId()).get();

		ClubUser clubUser = ClubUser.builder()
			.userId(clubApply.getUserId())
			.clubId(clubApply.getClubId())
			.userName(clubApply.getUserName())
			.profileImgUrl(profileImage.getImage().getUrl())
			.role(Role.MEMBER)
			//.profileImage(profileImage)
			.mannerTemp(user.getMannerTemp())
			.build();

		club.increaseMemberCount();
		clubRepository.save(club);
		clubApplyRepository.deleteByApplyId(applyId);

		return clubUserRepository.save(clubUser);
	}

	@Override
	@Transactional
	public void clubUserReject(Club club, User user, Long applyId) {

		if (!isOwner(club, user)) {

			throw new IllegalStateException();
		}
		clubApplyRepository.deleteByApplyId(applyId);
	}

	@Override
	public ClubUserAuthorityResponse checkUserAuthority(Long clubId, CustomUserDetails customUserDetails) {
		if (customUserDetails == null) {
			return ClubUserAuthorityResponse.of(Role.NONE);
		}

		Optional<ClubUser> optionalClubUser = clubUserRepository.findByUserIdAndClubId(customUserDetails.getUser().getId(), clubId);
		if (optionalClubUser.isEmpty()) {
			return ClubUserAuthorityResponse.of(Role.NONE);
		}
		ClubUser clubUser = optionalClubUser.get();

		return ClubUserAuthorityResponse.of(clubUser.getRole());
	}

	@Override
	public List<ClubMembersResponseDto> findMemberList(Club club) {
		List<ClubUser> memberList = getMembers(club.getClubId());
		List<ClubMembersResponseDto> clubMembersResponseDtoList = new ArrayList<>();

		for (ClubUser clubUser : memberList) {
			clubMembersResponseDtoList.add(ClubMembersResponseDto.of(clubUser));
		}
		return clubMembersResponseDtoList;
	}

	@Override
	@Transactional
	public void clubUserLeave(Club club, User user) {
		Long clubId = club.getClubId();
		Long userId = user.getId();
		List<ClubUser> members = getMembers(clubId);

		ClubUser clubUser = clubUserRepository.findByUserIdAndClubId(userId, clubId)
			.orElseThrow(() -> ModongException.from(ErrorCode.USER_NOT_FOUND));

		if (clubUser.getUserId().equals(userId) && members.contains(clubUser)) {
			members.remove(clubUser);
			clubUserRepository.deleteByUserIdAndClubId(userId, clubId);
		} else {
			throw new NoSuchElementException();
		}
	}

	@Override
	@Transactional
	public void clubUserExpel(Club club, User user, Long clubUserId) {
		Long clubId = club.getClubId();
		List<ClubUser> members = getMembers(clubId);
		ClubUser clubUser = clubUserRepository.findByClubUserId(clubUserId)
			.orElseThrow(() -> ModongException.from(ErrorCode.USER_NOT_FOUND));

		if (isOwner(club, user) && members.contains(clubUser)) {
			members.remove(clubUser);
			clubUserRepository.deleteByUserIdAndClubId(clubUser.getUserId(), clubId);
		} else {
			throw new NoSuchElementException();
		}
	}

	@Override
	public ClubUser clubUserReport(Club club, User user) {
		return null;
	}

	public List<ClubUser> getMembers(Long clubId) {

		Club club = this.clubRepository.findById(clubId)
			.orElseThrow(() -> ModongException.from(ErrorCode.CLUB_NOT_FOUND));

		return club.getMembers();
	}

	public boolean isOwner(Club club, User owner) {
		return club.getOwner().getId().equals(owner.getId());
	}
}
