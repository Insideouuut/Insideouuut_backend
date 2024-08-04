package com.goorm.insideout.club.dto.responseDto;

import com.goorm.insideout.club.dto.ClubDetailOwnerDto;
import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.club.entity.ClubApply;
import com.goorm.insideout.user.domain.User;

import lombok.Getter;

@Getter
public class ClubListResponseDto {
	private Long clubId;
	private String category;
	private Long ownerId;
	private String clubName;
	private String content;

	public static ClubListResponseDto of(Club club){
		return new ClubListResponseDto(club);
	}

	public ClubListResponseDto(Club club) {
		this.clubId = club.getClubId();
		this.category = club.getCategory();
		this.ownerId = club.getOwner().getId();
		this.clubName = club.getClubName();
		this.content = club.getContent();
	}
}
