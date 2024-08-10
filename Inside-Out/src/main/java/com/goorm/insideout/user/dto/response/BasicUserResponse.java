package com.goorm.insideout.user.dto.response;

import java.math.BigDecimal;

import com.goorm.insideout.image.dto.response.ImageResponse;
import com.goorm.insideout.user.domain.User;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class BasicUserResponse {

	private String nickname;
	private BigDecimal mannerTemp;
	private ImageResponse profileImage;

	public static BasicUserResponse from(User user) {
		BasicUserResponse response = new BasicUserResponse();
		response.nickname = user.getNickname();
		response.mannerTemp = user.getMannerTemp();
		response.profileImage = ImageResponse.from(user.getProfileImage().getImage());

		return response;
	}
}
