package com.goorm.insideout.club.dto.responseDto;

import java.time.LocalDateTime;

import com.goorm.insideout.club.entity.Club;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClubModifyResponseDto {

	protected Long clubId;

	protected String clubName;

	protected String category;

	protected LocalDateTime createdAt;
	protected String content;

	protected String date;

	protected String region;
	protected String question;


	protected Integer memberLimit;
	protected Integer price;
	protected Integer ageLimit;

	protected String clubImgUrl;

	public static ClubModifyResponseDto of(Club club){
		return new ClubModifyResponseDto(club);
	}

	public ClubModifyResponseDto (Club club){
		this.setClubId(club.getClubId());
		this.setClubName(club.getClubName());
		this.setClubImgUrl(club.getClubImg());
		this.setCreatedAt(club.getCreatedAt());
		this.setContent(club.getContent());
		this.setDate(club.getDate());
		this.setRegion(club.getRegion());
		this.setQuestion(club.getQuestion());
		this.setMemberLimit(club.getMemberLimit());
		this.setPrice(club.getPrice());
		this.setAgeLimit(club.getAgeLimit());

	}
}
