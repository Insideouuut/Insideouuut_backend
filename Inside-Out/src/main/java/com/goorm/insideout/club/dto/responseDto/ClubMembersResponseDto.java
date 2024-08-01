package com.goorm.insideout.club.dto.responseDto;

import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.club.entity.ClubUser;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClubMembersResponseDto {

	private String userName;
	private String profileImgUrl;
	private String mannerTemp;

	public static ClubMembersResponseDto of(ClubUser clubUser){
		return new ClubMembersResponseDto(clubUser);
	}

	public ClubMembersResponseDto(ClubUser clubUser){

		this.userName = clubUser.getUserName();
		this.profileImgUrl = clubUser.getProfileImgUrl();
		this.mannerTemp = clubUser.getMannerTemp();

	}
}
