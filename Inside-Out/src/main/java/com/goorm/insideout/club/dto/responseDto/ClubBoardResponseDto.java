package com.goorm.insideout.club.dto.responseDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.goorm.insideout.club.dto.ClubBoardDto;
import com.goorm.insideout.club.entity.Club;

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
	Integer memberCunt;
	Integer price;
	private Integer ageLimit;

	private String clubImgUrl;


	Boolean isRecruiting = false;


	public static ClubBoardResponseDto of(Club club){
		ClubBoardResponseDto res = new ClubBoardResponseDto();

		res.setClubId(club.getClubId());
		res.setClubName(club.getClubName());
		res.setClubImgUrl(club.getClubImg());
		res.setCategory(club.getCategory());
		res.setCreatedAt(club.getCreatedAt());
		res.setContent(club.getContent());
		res.setDate(club.getDate());
		res.setRegion(club.getRegion());
		res.setQuestion(club.getQuestion());
		res.setMemberLimit(club.getMemberLimit());

		res.setMemberCunt(club.getMemberCunt());
		res.setPrice(club.getPrice());
		res.setAgeLimit(club.getAgeLimit());

		if(club.getMemberLimit() > club.getMemberCunt()){
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
