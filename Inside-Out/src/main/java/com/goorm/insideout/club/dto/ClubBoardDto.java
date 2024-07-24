package com.goorm.insideout.club.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.goorm.insideout.club.entity.Club;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClubBoardDto {

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


	public static List<ClubBoardDto> of (List<Club> Clubs){
		List<ClubBoardDto> res = new ArrayList<>();

		for(Club Club : Clubs){
			ClubBoardDto ClubBoardDto = new ClubBoardDto();

			ClubBoardDto.setClubId(Club.getClubId());
			ClubBoardDto.setClubName(Club.getClubName());
			ClubBoardDto.setClubImgUrl(Club.getClubImg());
			ClubBoardDto.setCategory(Club.getCategory());
			ClubBoardDto.setCreatedAt(Club.getCreatedAt());
			ClubBoardDto.setContent(Club.getContent());
			ClubBoardDto.setDate(Club.getDate());
			ClubBoardDto.setRegion(Club.getRegion());
			ClubBoardDto.setQuestion(Club.getQuestion());
			ClubBoardDto.setMemberLimit(Club.getMemberLimit());

			ClubBoardDto.setMemberCunt(Club.getMemberCunt());
			ClubBoardDto.setPrice(Club.getPrice());
			ClubBoardDto.setAgeLimit(Club.getAgeLimit());

			if(Club.getMemberLimit() > Club.getMemberCunt()){
				ClubBoardDto.setIsRecruiting(true);
			}


			res.add(ClubBoardDto);
		}
		return res;
	}
}
