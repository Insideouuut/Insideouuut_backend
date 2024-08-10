package com.goorm.insideout.club.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.insideout.chatroom.domain.ChatRoom;
import com.goorm.insideout.chatroom.repository.ChatRoomRepository;
import com.goorm.insideout.club.dto.responseDto.ClubBoardResponseDto;
import com.goorm.insideout.club.dto.responseDto.ClubListResponseDto;
import com.goorm.insideout.club.entity.Category;
import com.goorm.insideout.club.entity.ClubApply;
import com.goorm.insideout.club.entity.ClubUser;
import com.goorm.insideout.club.repository.ClubApplyRepository;
import com.goorm.insideout.club.repository.ClubRepository;
import com.goorm.insideout.club.dto.requestDto.ClubRequestDto;
import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.club.repository.ClubUserRepository;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.meeting.domain.Role;
import com.goorm.insideout.meeting.dto.request.SearchRequest;
import com.goorm.insideout.image.domain.ProfileImage;
import com.goorm.insideout.image.repository.ProfileImageRepository;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.user.repository.UserRepository;
import com.goorm.insideout.userchatroom.repository.UserChatRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {

	private final ClubRepository clubRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final UserChatRoomRepository userChatRoomRepository;
	private final ClubUserRepository clubUserRepository;
	private final ClubApplyRepository clubApplyRepository;
	private final ProfileImageRepository profileImageRepository;

	String domainPrefix = "https://insideout.site:8082/resources/upload/images/club_image/";

	@Override
	@Transactional
	public Club createClub(ClubRequestDto clubRequestDto, User user) {

		Club club = clubBuilder(clubRequestDto, user);
		clubRequestDto.setEnum(club);
		club.setCreatedAt(LocalDateTime.now());

		clubRepository.save(club);

		ProfileImage profileImage = profileImageRepository.findByUserId(user.getId()).get();

		ClubUser clubUser = ClubUser.builder()
			.userId(user.getId())
			.clubId(club.getClubId())
			.userName(user.getName())
			.profileImgUrl(profileImage.getImage().getUrl())
			.role(Role.HOST)
			//.profileImage(profileImage)
			.mannerTemp(user.getMannerTemp())
			.build();

		clubUserRepository.save(clubUser);

		return club;
	}


	@Override
	public Club findByClubId(Long clubId) {


		return clubRepository.findById(clubId).orElseThrow(
			() -> ModongException.from(ErrorCode.CLUB_NOT_FOUND));
	}

	@Override
	public ClubBoardResponseDto findClubBoard(Long clubId) {
		Club byClubId = findByClubId(clubId);

		return ClubBoardResponseDto.of(byClubId);
	}

	@Override
	@Transactional
	public void deleteClub(Long clubId) {

		clubRepository.deleteById(clubId);
	}

	@Override
	@Transactional
	public Club modifyClub(ClubRequestDto clubRequestDto, User user, Club club) {
		Long clubId = club.getClubId();

		Club modify_club = clubBuilder(clubRequestDto, user);

		clubRequestDto.setEnum(modify_club);
		modify_club.setMemberCount(club.getMemberCount());
		modify_club.setCreatedAt(club.getCreatedAt());
		modify_club.setClubId(clubId);
		modify_club.setChat_room_id(club.getChat_room_id());
		modify_club.setChatRoom(club.getChatRoom());

		return clubRepository.save(modify_club);

	}

	@Override
	public List<ClubBoardResponseDto> findAllClubDesc(){

		return clubRepository.findAllByOrderByClubIdDesc().stream()
			.map(ClubBoardResponseDto::new)
			.toList();
	}

	// 정렬 타입에 따른 조회
	@Override
	public List<ClubBoardResponseDto> findBySortType(SearchRequest condition) {
		return clubRepository.findBySortType(condition);
	}

	// 검색 조건 및 정렬 타입에 따른 조회
	@Override
	public List<ClubBoardResponseDto> findByConditionAndSortType(SearchRequest condition) {
		return clubRepository.findByConditionAndSortType(condition);
	}

	@Override
	public List<ClubListResponseDto> findByCategory(String category) {

		return clubRepository.findByCategoryJQL(category).stream()
			.map(ClubListResponseDto::new)
			.collect(Collectors.toList());
	}

	@Override
	public List<ClubListResponseDto> findMyClub(Long userId) {
		return clubRepository.belongToClub(userId).stream()
			.map(ClubListResponseDto::new)
			.collect(Collectors.toList());
	}

	@Override
	public List<ClubListResponseDto> findMyApplyClub(Long userId) {
		List<ClubApply> byUserIdJQL = clubApplyRepository.findByUserIdJQL(userId);
		List<Club> clubList = new ArrayList<>();

		for (ClubApply clubApply : byUserIdJQL) {
			Long clubId = clubApply.getClubId();
			clubList.add(findByClubId(clubId));
		}

		return clubList.stream()
			.map(ClubListResponseDto::new)
			.collect(Collectors.toList());
	}

	public Club clubBuilder(ClubRequestDto ClubRequestDto, User user) {

		return Club.builder()
			.clubName(ClubRequestDto.getName())
			.categoryDetail(ClubRequestDto.getCategoryDetail())
			.content(ClubRequestDto.getIntroduction())
			.date(ClubRequestDto.getDate())
			.region(ClubRequestDto.getActivityRegion())
			.joinQuestions(ClubRequestDto.getJoinQuestions())
			.memberLimit(ClubRequestDto.getParticipantLimit())
			.memberCount(1)
			.hasMembershipFee(ClubRequestDto.isHasMembershipFee())
			.price(ClubRequestDto.getMembershipFeeAmount())
			.minAge(ClubRequestDto.getMinAge())
			.maxAge(ClubRequestDto.getMaxAge())
			.rules(ClubRequestDto.getRules())
			.owner(user)
			.build();

	}

	@Override
	public Club ownClub(Long clubId, Long userId) {

		return clubRepository.findByClubIdAndUserIdJQL(clubId, userId)
			.orElseThrow(() -> ModongException.from(ErrorCode.CLUB_NOT_AUTHORIZED));

	}

	public List<ClubUser> getMembers(Long clubId) {

		Club club = this.clubRepository.findById(clubId)
			.orElseThrow(() -> ModongException.from(ErrorCode.CLUB_NOT_FOUND));

		List<ClubUser> members = club.getMembers();

		return members;
	}

	@Override
	@Transactional
	public void setChatRoom(Club club, ChatRoom chatRoom) {
		club.setChatRoom(chatRoom);
		club.setChat_room_id(chatRoom.getId());
		clubRepository.save(club);
	}

}
