package com.goorm.insideout.club.dto;

import com.goorm.insideout.club.entity.ClubUser;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ClubUserDto {

	private Long member_id;

	private String username;
	private String email;

	private String password;

	private Long phone_number;
	private int age;

	private String region;

	private String loginId;



	public ClubUserDto(ClubUser clubUser){
		/*
		this.member_id = clubUser.getClubUserId();
		this.loginId = clubUser.getLoginId();
		this.username = clubUser.getUsername();
		this.email = clubUser.getEmail();
		*/

	}
}
