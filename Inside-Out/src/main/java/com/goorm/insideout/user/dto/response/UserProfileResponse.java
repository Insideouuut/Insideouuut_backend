package com.goorm.insideout.user.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.goorm.insideout.image.dto.response.ImageResponse;
import com.goorm.insideout.meeting.domain.Category;
import com.goorm.insideout.user.domain.Gender;
import com.goorm.insideout.user.domain.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfileResponse {
	private Long id;
	private String email;
	private String name;
	private String nickname;
	private ImageResponse profileImage;
	@JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss")
	private LocalDate birthDate;
	private String phoneNumber;
	private BigDecimal mannerTemp;
	private Set<Category> interests;
	private Set<String> locations;
	private boolean isLocationVerified;
	private Gender gender;

	public static UserProfileResponse of(User user, ImageResponse profileImage) {
		UserProfileResponse response = new UserProfileResponse();

		response.id = user.getId();
		response.email = user.getEmail();
		response.name = user.getName();
		response.nickname = user.getNickname();
		response.profileImage = profileImage;
		response.birthDate = user.getBirthDate();
		response.phoneNumber = user.getPhoneNumber();
		response.mannerTemp = user.getMannerTemp();
		response.interests = user.getInterests();
		response.locations = user.getLocations();
		response.isLocationVerified = user.isLocationVerified();
		response.gender = user.getGender();

		return response;
	}
 }
