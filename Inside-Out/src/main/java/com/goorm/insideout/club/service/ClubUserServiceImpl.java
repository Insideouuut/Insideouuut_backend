package com.goorm.insideout.club.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.insideout.club.dto.responseDto.ClubMembersResponseDto;
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
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ClubUserServiceImpl implements ClubUserService {

	private final ClubUserRepository clubUserRepository;
	private final ClubRepository clubRepository;
	private final ClubApplyRepository clubApplyRepository;
	private final UserRepository userRepository;
	private final ProfileImageRepository profileImageRepository;

	/*
	@Override
	public ClubUser clubUserFind(Long userId, Long clubId) {
		return clubUserRepository.findByUserIdAndClubId(userId,clubId).orElse(null);
	}

	 */

	@Override
	public ClubUser clubUserFind(Long userId, Long clubId) {

		return clubUserRepository.findByUserIdAndClubId(userId, clubId)
			.orElseThrow(() -> ModongException.from(ErrorCode.CLUB_NOT_AUTHORIZED));
	}

	@Modifying(clearAutomatically = true)
	@Transactional
	@Override
	public void clubUserDelete(Long userId, Long clubId) {
		clubUserRepository.deleteByUserIdAndClubId(userId,clubId);
	}

	@Override
	@Transactional
	public ClubUser clubUserAccept(Club club, User user, Long applyId) {
		ClubApply clubApply = clubApplyRepository.findByApplyId(applyId).orElseThrow(()-> ModongException.from(
			ErrorCode.USER_NOT_FOUND));
		Integer memberLimit = club.getMemberLimit();
		Integer memberCount = club.getMemberCount();

		if(!isOwner(club, user)){
			throw new NoSuchElementException();
		}
		if(memberCount >= memberLimit){
			throw new IllegalStateException();
		}

		ProfileImage profileImage = profileImageRepository.findByUserId(user.getId()).get();

		ClubUser clubUser = ClubUser.builder()
			.userId(clubApply.getUserId())
			.clubId(clubApply.getClubId())
			.userName(clubApply.getUserName())
			.profileImgUrl(profileImage.getImage().getUrl())
			//.profileImage(profileImage)
			.mannerTemp(user.getMannerTemp())
			.build();

		System.out.println("clubUser.getProfileImgUrl() = " + clubUser.getProfileImgUrl());

		club.increaseMemberCount();
		clubRepository.save(club);
		clubApplyRepository.deleteByApplyId(applyId);

		return clubUserRepository.save(clubUser);
	}

	@Override
	public void clubUserReject(Club club, User user, Long applyId) {
		Long clubId = club.getClubId();
		Long userId = user.getId();

		if(!isOwner(club, user)){

			throw new NoSuchElementException();
		}
		clubApplyRepository.deleteByApplyId(applyId);
	}

	@Override
	public List<ClubMembersResponseDto> findMemberList(Club club) {
		List<ClubUser> memberList = getMembers(club.getClubId());
		List<ClubMembersResponseDto> clubMembersResponseDtoList = new ArrayList<>();

		for(ClubUser clubUser : memberList){
			clubMembersResponseDtoList.add(ClubMembersResponseDto.of(clubUser));
		}
		return clubMembersResponseDtoList;
	}

	@Override
	public void clubUserLeave(Club club, User user) {
		Long clubId = club.getClubId();
		Long userId = user.getId();
		List<ClubUser> members = getMembers(clubId);
		Optional<ClubUser> clubUser = clubUserRepository.findByUserIdAndClubId(userId, clubId);

		if(clubUser.isPresent()){
			if(clubUser.get().getUserId().equals(userId) && members.contains(clubUser.get())){
				members.remove(clubUser.get());
				clubUserRepository.deleteByUserIdAndClubId(userId,clubId);
			}else {
				throw new NoSuchElementException();
			}
		}else {
			throw new NullPointerException();
		}
	}

	@Override
	public void clubUserExpel(Club club, User user, Long clubUserId) {
		Long clubId = club.getClubId();
		List<ClubUser> members = getMembers(clubId);
		Optional<ClubUser> clubUser = clubUserRepository.findByClubUserId(clubUserId);


		if(clubUser.isPresent()){
			if(isOwner(club, user) && members.contains(clubUser.get())){
				members.remove(clubUser.get());
				clubUserRepository.deleteByUserIdAndClubId(clubUser.get().getUserId(), clubId);
			}else {
				throw new NoSuchElementException();
			}
		}else {
			throw new NullPointerException();
		}
	}

	@Override
	public ClubUser clubUserReport(Club club, User user) {
		return null;
	}

	@Transactional(readOnly = true)
	public List<ClubUser> getMembers(Long clubId) {
		if(!this.clubRepository.findById(clubId).isPresent()){
			throw new NoSuchElementException();
		}
		Club club = this.clubRepository.findById(clubId).get();

		List<ClubUser> members = club.getMembers();

		return members;
	}

	@Transactional
	public boolean isOwner(Club club, User owner){
		return club.getOwner().getId().equals(owner.getId());
	}
}
