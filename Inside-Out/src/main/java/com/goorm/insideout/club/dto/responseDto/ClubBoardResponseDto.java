package com.goorm.insideout.club.dto.responseDto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.image.dto.response.ImageResponse;
import com.querydsl.core.annotations.QueryProjection;

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

	@JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss")
	private LocalDateTime date;

	private String region;
	private String question;


	private Integer memberLimit;
	private Integer memberCount;
	private Integer price;
	private Integer ageLimit;

	private List<ImageResponse> images;
	private Long chatRoomId;


	Boolean isRecruiting = false;

	@QueryProjection
	public ClubBoardResponseDto(Club club) {
		this.clubId = club.getClubId();
		this.clubName = club.getClubName();
		this.category = club.getCategory();
		this.createdAt = club.getCreatedAt();
		this.content = club.getContent();
		this.date = club.getDate();
		this.region = club.getRegion();
		this.question = club.getQuestion();
		this.memberLimit = club.getMemberLimit();
		this.memberCount = club.getMemberCount();
		this.price = club.getPrice();
		this.ageLimit = club.getAgeLimit();
		this.chatRoomId = club.getChat_room_id();
		this.images = club.getImages()
			.stream()
			.map(image -> ImageResponse.from(image.getImage()))
			.toList();

		if(club.getMemberLimit() > club.getMemberCount()){
			this.isRecruiting = true;
		}
	}

	public static ClubBoardResponseDto of(Club club){
		return new ClubBoardResponseDto(club);
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
