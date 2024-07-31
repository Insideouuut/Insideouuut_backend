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
import com.goorm.insideout.user.domain.User;

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

	@Override
	public ClubUser clubUserFind(Long userId, Long clubId) {
		return clubUserRepository.findByUserIdAndClubId(userId,clubId).orElse(null);
	}

	@Modifying(clearAutomatically = true)
	@Transactional
	@Override
	public void clubUserDelete(Long userId, Long clubId) {
		clubUserRepository.deleteByUserIdAndClubId(userId,clubId);
	}

	/*
	@Override
	public ClubUser clubUserApply(Club club, User user, ClubApplyRequestDto clubApplyRequestDto) {
		ClubUser clubUser = ClubUser.builder()
			.userId(user.getId())
			.clubId(club.getClubId())
			.userName(user.getName())
			//.profileImgUrl(user.getProfileImgUrl)
			//.mannerTemp(user.getMannerTemp)
			.build();

		List<ClubUser> byClubIdJQL = clubUserRepository.findByClubIdJQL(club.getClubId());
		List<ClubUser> members = getMembers(club.getClubId());
		System.out.println("byClubIdJQL = " + byClubIdJQL);
		System.out.println("members = " + members);

		return clubUserRepository.save(clubUser);
	}

	 */

	/*
	@Override
	public void clubUserAccept(Club club, User user, Long applyId) {
		List<ClubUser> members = club.getMembers();
		Optional<ClubUser> clubUser = clubUserRepository.findByApplyId(applyId);

		if(!user.getId().equals(club.getOwner().getId())){
			System.out.println("user = " + user);
			System.out.println("club.getOwner() = " + club.getOwner());
			System.out.println("user.getId() = " + user.getId());
			System.out.println("club.getOwner().getId() = " + club.getOwner().getId());

			throw new NoSuchElementException();
		}
		if(clubUser.isEmpty()){
			throw new NullPointerException();
		}else {
			members.add(clubUser.get());
		}
	}

	 */

	@Override
	@Transactional
	public ClubUser clubUserAccept(Club club, User user, Long applyId) {
		ClubApply clubApply = clubApplyRepository.findByApplyId(applyId).get();
		Integer memberLimit = club.getMemberLimit();
		Integer memberCount = club.getMemberCount();

		if(!user.getId().equals(club.getOwner().getId())){
			throw new NoSuchElementException();
		}
		if(memberCount >= memberLimit){
			throw new IllegalStateException();
		}
		System.out.println("memberCount = " + club.getMemberCount());
		ClubUser clubUser = ClubUser.builder()
			.userId(clubApply.getUserId())
			.clubId(clubApply.getClubId())
			.userName(clubApply.getUserName())
			//.profileImgUrl(user.getProfileImgUrl)
			//.mannerTemp(user.getMannerTemp)
			.build();
		club.increaseMemberCount();
		System.out.println("memberCount = " + club.getMemberCount());
		clubRepository.save(club);
		clubApplyRepository.deleteByApplyId(applyId);

		return clubUserRepository.save(clubUser);
	}

	@Override
	public void clubUserReject(Club club, User user, Long applyId) {
		Long clubId = club.getClubId();
		Long userId = user.getId();

		if(!user.getId().equals(club.getOwner().getId())){

			throw new NoSuchElementException();
		}
		clubApplyRepository.deleteByApplyId(applyId);
	}

	/*
	@Override
	public List<ClubApplyResponseDto> findApplyList(Club club, User user) {
		List<ClubUser> byClubIdJQL = clubUserRepository.findByClubIdJQL(club.getClubId());
		List<ClubApplyResponseDto> applyList = new ArrayList<>();

		List<ClubUser> members = getMembers(club.getClubId());

		if(!user.getId().equals(club.getOwner().getId())){
			System.out.println("user = " + user);
			System.out.println("club.getOwner() = " + club.getOwner());
			System.out.println("user.getId() = " + user.getId());
			System.out.println("club.getOwner().getId() = " + club.getOwner().getId());

			throw new NoSuchElementException();
		}
		System.out.println("byClubIdJQL = " + byClubIdJQL);
		System.out.println("members = " + members);
		for(ClubUser clubUser: byClubIdJQL){

			if(!members.contains(clubUser)){
				ClubApplyResponseDto clubApplyResponseDto = ClubApplyResponseDto.of(clubUser);
				applyList.add(clubApplyResponseDto);
			}
		}

		return applyList;
	}

	 */


	@Override
	public List<ClubMembersResponseDto> findMemberList(Club club) {
		List<ClubUser> memberList = getMembers(club.getClubId());
		List<ClubMembersResponseDto> clubMembersResponseDtoList = new ArrayList<>();

		for(ClubUser clubUser : memberList){
			clubMembersResponseDtoList.add(ClubMembersResponseDto.of(clubUser));
		}
		return clubMembersResponseDtoList;
	}


/*
	@Override
	public List<ClubDetailResponseDto> findMemberList(Club club) {
		List<ClubUser> byClubIdJQL = clubUserRepository.findByClubIdJQL(club.getClubId());
		List<ClubDetailResponseDto> memberList = new ArrayList<>();


		for(ClubUser clubUser: byClubIdJQL){
			ClubDetailResponseDto clubDetailResponseDto = memberValidate(club, clubUser);
			if(clubDetailResponseDto.getRole().equals("member")){
				memberList.add(clubDetailResponseDto);
			}
		}



		return memberList;
	}

 */

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
		Long ownerId = user.getId();
		List<ClubUser> members = getMembers(clubId);
		Optional<ClubUser> clubUser = clubUserRepository.findByClubUserId(clubUserId);


		if(clubUser.isPresent()){
			if(club.getOwner().getId().equals(ownerId) && members.contains(clubUser.get())){
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

	/*
	//클럽멤버 구분할때 씀 참여대기자보기에 씀
	@Transactional
	public ClubDetailResponseDto memberValidate(Club club, ClubUser clubUser){
		ClubDetailResponseDto res = new ClubDetailResponseDto();

		if(club == null){
			res.setClubData(null);
			res.setRole(null);
		}
		else {
			res.setClubData(ClubMemberDetail.of(club));
			res.setRole("outsider");
			if (club.getOwner().getId().equals(clubUser.getUserId()))
				res.setRole("owner");
			else {
				for (ClubUser member : club.getMembers()) {
					if (member.getUser().getId().equals(clubUser.getUserId())) {
						res.setRole("member");
						break;
					}
				}
			}
		}
		return res;
	}

	 */

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
