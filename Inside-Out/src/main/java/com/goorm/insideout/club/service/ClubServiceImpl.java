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
import com.goorm.insideout.club.entity.ClubApply;
import com.goorm.insideout.club.entity.ClubUser;
import com.goorm.insideout.club.repository.ClubApplyRepository;
import com.goorm.insideout.club.repository.ClubRepository;
import com.goorm.insideout.club.dto.requestDto.ClubRequestDto;
import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.club.repository.ClubUserRepository;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.image.domain.ProfileImage;
import com.goorm.insideout.image.repository.ProfileImageRepository;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.user.repository.UserRepository;
import com.goorm.insideout.userchatroom.repository.UserChatRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService{

	private final ClubRepository clubRepository;
	private final ClubUserRepository clubUserRepository;
	private final ClubApplyRepository clubApplyRepository;
	private final UserRepository userRepository;
	private final ProfileImageRepository profileImageRepository;

	String domainPrefix = "https://insideout.site:8082/resources/upload/images/club_image/";



	@Override
	public Club createClub(ClubRequestDto clubRequestDto, /*MultipartFile multipartFile,*/ User user) throws IOException {
		String clubImgUrl;
		/*
		if (multipartFile == null || multipartFile.isEmpty()) {
			// 기본 이미지 경로 설정
			clubImgUrl = null;
		} else {
			String profileImgSaveUrl = "/var/webapps/upload/clubs/club_image/" + clubRequestDto.getClubName() + user.getId() + "_" + multipartFile.getOriginalFilename();

			File file = new File(profileImgSaveUrl);

			multipartFile.transferTo(file.toPath());
			clubImgUrl = domainPrefix + clubRequestDto.getClubName() + user.getId() + "_" + multipartFile.getOriginalFilename();
		}

		 */

		Club club = clubRepository.save(clubBuilder(clubRequestDto, /*clubImgUrl,*/ user));

		ProfileImage profileImage = profileImageRepository.findByUserId(user.getId()).get();

		club.setCreatedAt(LocalDateTime.now());

		ClubUser clubUser = ClubUser.builder()
			.userId(user.getId())
			.clubId(club.getClubId())
			.userName(user.getName())
			//.profileImgUrl(user.getProfileImgUrl)
			.profileImage(profileImage)
			.mannerTemp(user.getMannerTemp())
			.build();
		clubUserRepository.save(clubUser);


		return club;
	}

	@Override
	public Club findByClubId(Long clubId) {

		return clubRepository.findById(clubId).orElseThrow(null);
	}

	@Override
	public ClubBoardResponseDto findClubBoard(Long clubId, User user) {
		Club byClubId = findByClubId(clubId);

		return ClubBoardResponseDto.of(byClubId, user);
	}

	@Override
	public void deleteClub(Long clubId) {
		Club club = clubRepository.getById(clubId);

		clubRepository.deleteById(clubId);
	}

	@Override
	public Club modifyClub(ClubRequestDto clubRequestDto, User user, Club club) throws
		IOException {
		Long clubId = club.getClubId();

		String clubImgUrl;

		/*
		if (club.getClubImg() != null && clubRequestDto.getClubImgUrl() == null) {
			File old_file = new File("/var/webapps/upload/images/team_image/" + getFileNameFromURL(club.getClubImg()));
			if (old_file.exists()) {
				old_file.delete();
			}
		}
		//이미지가 비어있으면 기존 이미지로
		if (multipartFile == null || multipartFile.isEmpty()) {

			clubImgUrl = clubRequestDto.getClubImgUrl();

		} else {    // 아니면 새 이미지로
			String profileImgSaveUrl = "/var/webapps/upload/images/team_image/" + clubRequestDto.getClubName() + user.getId() + "_" + multipartFile.getOriginalFilename();

			File file = new File(profileImgSaveUrl);
			multipartFile.transferTo(file);
			clubImgUrl = domainPrefix + clubRequestDto.getClubName() + user.getId() + "_" + multipartFile.getOriginalFilename();
		}

		 */

		Club modify_club = clubBuilder(clubRequestDto, user);
		modify_club.setMemberCount(club.getMemberCount());
		modify_club.setCreatedAt(club.getCreatedAt());
		modify_club.setClubId(clubId);

		return clubRepository.save(modify_club);

	}

	@Override
	public List<ClubListResponseDto> findAllClubDesc(){

		return clubRepository.findAllByOrderByClubIdDesc().stream()
			.map(ClubListResponseDto::new)
			.collect(Collectors.toList());
	}

	/*
	@Override
	public Club belongToClub(Long userId) {
		return clubRepository.belongToClub(userId).orElseThrow(()->ModongException.from(ErrorCode.CLUB_NOT_AUTHORIZED));
	}

	 */


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

		for(ClubApply clubApply : byUserIdJQL){
			Long clubId = clubApply.getClubId();
			clubList.add(findByClubId(clubId));
		}

		return clubList.stream()
			.map(ClubListResponseDto::new)
			.collect(Collectors.toList());
	}


	/*
	@Override
	public List<Club> findAllClub(List<ClubInfo> clubInfos) {
		List<Club> clubs = new ArrayList<>();

		for (ClubInfo clubInfo : clubInfos) {
			clubs.add(clubTransfer(clubInfo));
		}
		return clubs;
	}



	public Club clubTransfer(ClubInfo clubInfo) {

		Club club = Club.builder()
			.clubId(clubInfo.getClubId())
			.clubName(clubInfo.getClubName())
			.category(clubInfo.getCategory())
			.modifiedAt(clubInfo.getModifiedAt())
			.content(clubInfo.getContent())
			.date(clubInfo.getDate())
			.region(clubInfo.getRegion())
			.question(clubInfo.getQuestion())
			.memberLimit(clubInfo.getMemberLimit())
			.memberCunt(clubInfo.getMemberCunt())
			.price(clubInfo.getPrice())
			.ageLimit(clubInfo.getAgeLimit())
			.clubImg(clubInfo.getClubImg())
			.build();

		return club;

	}

	 */
	public Club clubBuilder(ClubRequestDto ClubRequestDto, User user) {

		return Club.builder()
			.clubName(ClubRequestDto.getName())
			.category(ClubRequestDto.getCategory())
			.categoryDetail(ClubRequestDto.getCategoryDetail())
			.level(ClubRequestDto.getLevel())
			.content(ClubRequestDto.getIntroduction())
			.date(ClubRequestDto.getDate())
			.region(ClubRequestDto.getActivityRegion())
			.joinQuestions(ClubRequestDto.getJoinQuestions())
			.memberLimit(ClubRequestDto.getParticipantLimit())
			.memberCount(1)
			.hasMembershipFee(ClubRequestDto.isHasMembershipFee())
			.price(ClubRequestDto.getMembershipFeeAmount())
			.hasGenderRatio(ClubRequestDto.getHasGenderRatio())
			.ratio(ClubRequestDto.getRatio())
			.ageRange(ClubRequestDto.getAgeRange())
			.rules(ClubRequestDto.getRules())
			.owner(user)
			.build();

	}

	// url 에서 파일 이름 추출
	public static String getFileNameFromURL(String url) {

		return url.substring(url.lastIndexOf('/') + 1, url.length());

	}

	@Override
	public Club ownClub(Long clubId, Long userId) {

		return clubRepository.findByClubIdAndUserIdJQL(clubId, userId).orElseThrow(()->ModongException.from(ErrorCode.CLUB_NOT_AUTHORIZED));

	}

	@Transactional(readOnly = true)
	public List<ClubUser> getMembers(Long clubId) {

		Club club = this.clubRepository.findById(clubId).orElseThrow(()->ModongException.from(ErrorCode.CLUB_NOT_FOUND));

		List<ClubUser> members = club.getMembers();

		return members;
	}

	@Override
	@Transactional
	public void setChatRoom(Club club, ChatRoom chatRoom){
		club.setChatRoom(chatRoom);
		club.setChat_room_id(chatRoom.getId());
		clubRepository.save(club);
	}

}
