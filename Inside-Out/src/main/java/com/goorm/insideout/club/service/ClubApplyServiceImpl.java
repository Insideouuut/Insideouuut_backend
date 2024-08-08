package com.goorm.insideout.club.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.insideout.club.dto.requestDto.ClubApplyRequestDto;
import com.goorm.insideout.club.dto.responseDto.ClubApplyResponseDto;
import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.club.entity.ClubApply;
import com.goorm.insideout.club.entity.ClubUser;
import com.goorm.insideout.club.repository.ClubApplyRepository;
import com.goorm.insideout.club.repository.ClubRepository;
import com.goorm.insideout.club.repository.ClubUserRepository;
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
public class ClubApplyServiceImpl implements ClubApplyService{

	private final ClubApplyRepository clubApplyRepository;
	private final ClubRepository clubRepository;
	private final UserRepository userRepository;
	private final ProfileImageRepository profileImageRepository;

	@Override
	public ClubApply findClubApplyByUserIDAndClubId(Long userId, Long clubId) {
		return clubApplyRepository.findByUserIdAndClubId(userId,clubId).orElse(null);
	}

	@Modifying(clearAutomatically = true)
	@Transactional
	@Override
	public void clubApplyDelete(Long userId, Long clubId) {
		clubApplyRepository.deleteByUserIdAndClubId(userId,clubId);

	}

	@Override
	public ClubApply clubApply(Club club, User user, ClubApplyRequestDto clubApplyRequestDto) {
		if(clubApplyRepository.findByUserIdAndClubId(user.getId(), club.getClubId()).isPresent()){
			throw new IllegalStateException();
		}

		List<ClubUser> members = getMembers(club.getClubId());

		for(ClubUser clubUser : members){
			if(user.getId().equals(clubUser.getUserId())){
				throw new IllegalStateException();
			}
		}
		ProfileImage profileImage = profileImageRepository.findByUserId(user.getId()).get();

		ClubApply clubApply = ClubApply.builder()
			.userId(user.getId())
			.clubId(club.getClubId())
			.userName(user.getName())
			.profileImgUrl(profileImage.getImage().getUrl())
			//.profileImage(profileImage)
			.mannerTemp(user.getMannerTemp())
			.answer(clubApplyRequestDto.getAnswer())
			.build();

		return clubApplyRepository.save(clubApply);
	}


	@Override
	public List<ClubApplyResponseDto> findApplyList(Club club, User user) {
		List<ClubApply> byClubIdJQL = clubApplyRepository.findByClubIdJQL(club.getClubId());
		List<ClubApplyResponseDto> applyList = new ArrayList<>();

		List<ClubUser> members = getMembers(club.getClubId());

		if(!user.getId().equals(club.getOwner().getId())){

			throw new NoSuchElementException();
		}

		for(ClubApply clubApply: byClubIdJQL){
			ClubApplyResponseDto clubApplyResponseDto = ClubApplyResponseDto.of(clubApply);
			applyList.add(clubApplyResponseDto);
		}

		return applyList;
	}

	@Override
	public ClubApply findClubApplyById(Long applyId) {
		return clubApplyRepository.findByApplyId(applyId).get();
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
}
