package com.goorm.insideout.club.dto;

import com.goorm.insideout.user.domain.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClubDetailOwnerDto {
	private Long userId;
	private String userName;

	public static ClubDetailOwnerDto of(User user){
		ClubDetailOwnerDto res = new ClubDetailOwnerDto();
		res.setUserId(user.getId());
		res.setUserName(user.getName());
		return res;
	}
}
