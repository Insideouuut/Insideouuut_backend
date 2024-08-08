package com.goorm.insideout.club.dto.responseDto;

import java.math.BigDecimal;

import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.club.entity.ClubUser;
import com.goorm.insideout.image.domain.ProfileImage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClubMembersResponseDto {

	private String userName;
	private ProfileImage profileImage;
	//private String profileImgUrl;
	private BigDecimal mannerTemp;

	public static ClubMembersResponseDto of(ClubUser clubUser){
		return new ClubMembersResponseDto(clubUser);
	}

	public ClubMembersResponseDto(ClubUser clubUser){

		this.userName = clubUser.getUserName();
		this.profileImage = clubUser.getProfileImage();
		this.mannerTemp = clubUser.getMannerTemp();

	}
}
