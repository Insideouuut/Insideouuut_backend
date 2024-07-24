package com.goorm.insideout.club.dto.responseDto;

import com.goorm.insideout.club.dto.ClubDetailOwnerDto;
import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.user.domain.User;

import lombok.Getter;

@Getter
public class ClubListResponseDto {
	private Long clubId;
	User owner;
	private String clubName;
	private String content;

	public ClubListResponseDto(Club club) {
		this.clubId = club.getClubId();
		this.owner = club.getOwner();
		this.clubName = club.getClubName();
		this.content = club.getContent();
	}
}
