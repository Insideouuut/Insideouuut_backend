package com.goorm.insideout.club.dto.responseDto;

import java.time.LocalDateTime;
import java.util.List;

import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.image.dto.response.ImageResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClubBoardResponseDto {
	private Long clubId;

	private String clubName;

	private String category;

	private LocalDateTime createdAt;
	private String content;

	private String date;

	private String region;
	private String question;


	private Integer memberLimit;
	private Integer memberCount;
	private Integer price;
	private Integer ageLimit;

	private List<ImageResponse> images;
	private Long chatRoomId;


	Boolean isRecruiting = false;


	public static ClubBoardResponseDto of(Club club){
		ClubBoardResponseDto res = new ClubBoardResponseDto();

		res.setClubId(club.getClubId());
		res.setClubName(club.getClubName());
		res.images = club.getImages()
			.stream()
			.map(image -> ImageResponse.from(image.getImage()))
			.toList();
		res.setCategory(club.getCategory());
		res.setCreatedAt(club.getCreatedAt());
		res.setContent(club.getContent());
		res.setDate(club.getDate());
		res.setRegion(club.getRegion());
		res.setQuestion(club.getQuestion());
		res.setMemberLimit(club.getMemberLimit());

		res.setMemberCount(club.getMemberCount());
		res.setPrice(club.getPrice());
		res.setAgeLimit(club.getAgeLimit());
		res.setChatRoomId(club.getChat_room_id());

		if(club.getMemberLimit() > club.getMemberCount()){
			res.setIsRecruiting(true);
		}
		return res;
	}

	/*
	public static ClubBoardResponseDto of (Club club){

		ClubBoardResponseDto clubBoardResponseDto = new ClubBoardResponseDto();

		clubBoardResponseDto.setClubId(club.getClubId());
		clubBoardResponseDto.setClubName(club.getClubName());
		clubBoardResponseDto.setClubImgUrl(club.getClubImg());
		clubBoardResponseDto.setCategory(club.getCategory());
		clubBoardResponseDto.setCreatedAt(club.getCreatedAt());
		clubBoardResponseDto.setContent(club.getContent());
		clubBoardResponseDto.setDate(club.getDate());
		clubBoardResponseDto.setRegion(club.getRegion());
		clubBoardResponseDto.setQuestion(club.getQuestion());
		clubBoardResponseDto.setMemberLimit(club.getMemberLimit());

		clubBoardResponseDto.setMemberCunt(club.getMemberCunt());
		clubBoardResponseDto.setPrice(club.getPrice());
		clubBoardResponseDto.setAgeLimit(club.getAgeLimit());

		if(club.getMemberLimit() > club.getMemberCunt()){
			clubBoardResponseDto.setIsRecruiting(true);
		}

		return clubBoardResponseDto;
	}

	 */
}
