package com.goorm.insideout.club.dto;

import java.util.ArrayList;
import java.util.List;

import com.goorm.insideout.club.entity.ClubUser;
import com.goorm.insideout.user.domain.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClubDetailUserDto {
	private Long userId;
	private String userName;
	private String profileImgUrl;
	private String mannerTemp;

	public static List<ClubDetailUserDto> of(List<ClubUser> users){
		List<ClubDetailUserDto> res = new ArrayList<>();
		for(ClubUser userteam : users) {
			User user = userteam.getUser();
			ClubDetailUserDto clubDetailUserDto = new ClubDetailUserDto();
			clubDetailUserDto.setUserId(user.getId());
			clubDetailUserDto.setUserName(user.getName());
			/*
			clubDetailUserDto.setProfileImgUrl(user.getProfileImg());
			clubDetailUserDto.setMannerTemp(user.getMannerTemp());
			*/
			res.add(clubDetailUserDto);
		}
		return res;
	}
}
