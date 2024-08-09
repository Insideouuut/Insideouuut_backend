package com.goorm.insideout.club.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.insideout.chatroom.domain.ChatRoom;
import com.goorm.insideout.chatroom.repository.ChatRoomRepository;
import com.goorm.insideout.club.dto.responseDto.ClubBoardResponseDto;
import com.goorm.insideout.club.dto.responseDto.ClubListResponseDto;
import com.goorm.insideout.club.entity.ClubUser;
import com.goorm.insideout.club.repository.ClubRepository;
import com.goorm.insideout.club.dto.requestDto.ClubRequestDto;
import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.club.repository.ClubUserRepository;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.meeting.dto.request.SearchRequest;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.userchatroom.repository.UserChatRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService{

	private final ClubRepository clubRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final UserChatRoomRepository userChatRoomRepository;
	private final ClubUserRepository clubUserRepository;

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


		club.setCreatedAt(LocalDateTime.now());

		ClubUser clubUser = ClubUser.builder()
			.userId(user.getId())
			.clubId(club.getClubId())
			.userName(user.getName())
			//.profileImgUrl(user.getProfileImgUrl)
			//.mannerTemp(user.getMannerTemp)
			.build();
		clubUserRepository.save(clubUser);


		return club;
	}

	@Override
	public Club findByClubId(Long ClubId) {
		return clubRepository.findById(ClubId)
			.orElseThrow(()->ModongException.from(ErrorCode.CLUB_NOT_FOUND));
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
	public Club belongToClub(Long userId) {
		return clubRepository.belongToTeam(userId).orElseThrow(()->ModongException.from(ErrorCode.CLUB_NOT_AUTHORIZED));
	}


	@Override
	public List<ClubListResponseDto> findByCategory(String category) {
		return clubRepository.findByCategoryJQL(category).stream()
			.map(ClubListResponseDto::new)
			.toList();
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
			.clubName(ClubRequestDto.getClubName())
			.category(ClubRequestDto.getCategory())
			.content(ClubRequestDto.getContent())
			.date(ClubRequestDto.getDate())
			.region(ClubRequestDto.getRegion())
			.question(ClubRequestDto.getQuestion())
			.memberLimit(ClubRequestDto.getMemberLimit())
			.memberCount(1)
			.price(ClubRequestDto.getPrice())
			.ageLimit(ClubRequestDto.getAgeLimit())
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
