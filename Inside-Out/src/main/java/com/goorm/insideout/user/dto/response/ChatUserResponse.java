package com.goorm.insideout.user.dto.response;

import com.goorm.insideout.image.dto.response.ImageResponse;
import com.goorm.insideout.user.domain.User;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class ChatUserResponse {
	private Long id;
	private String nickname;
	private ImageResponse profileImage;

	public static ChatUserResponse from(User user) {
		ChatUserResponse response = new ChatUserResponse();

		response.id = user.getId();
		response.nickname = user.getNickname();
		response.profileImage = ImageResponse.from(user.getProfileImage().getImage());

		return response;
	}
}
