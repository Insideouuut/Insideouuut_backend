package com.goorm.insideout.auth.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.goorm.insideout.meeting.domain.Category;
import com.goorm.insideout.user.domain.Gender;
import com.goorm.insideout.user.domain.User;

import lombok.Getter;

@Getter
public class UserLoginResponse {
	private final Long userId;
	private final String profileImage;
	private final String email;
	private final String name;
	private final String nickname;
	private final String phoneNumber;
	private final Gender gender;
	private final String birthDate;
	private final BigDecimal mannerRating;
	private final List<Category> interests;
	private final List<String> locations;

	public UserLoginResponse(User user) {
		this.userId = user.getId();
		this.email = user.getEmail();
		this.profileImage = user.getProfileImage().getImage().getUrl();
		this.name = user.getName();
		this.nickname = user.getNickname();
		this.mannerRating = user.getMannerTemp();
		this.gender = user.getGender();
		this.phoneNumber = user.getPhoneNumber();
		this.birthDate = user.getBirthDate().toString();
		this.interests = user.getInterests().stream().toList();
		this.locations = user.getLocations().stream().toList();

	}
}
