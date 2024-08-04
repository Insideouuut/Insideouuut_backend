package com.goorm.insideout.club.dto;

import com.goorm.insideout.club.entity.ClubUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ClubUserDto {

	private Long clubUserid;

	private String username;

	private String profileImgUrl;

	private String mannerTemp;


	public ClubUserDto(ClubUser clubUser){

		this.clubUserid = clubUser.getClubUserId();
		this.username = clubUser.getUserName();
		this.profileImgUrl = clubUser.getProfileImgUrl();
		this.mannerTemp = clubUser.getMannerTemp();


	}
}
