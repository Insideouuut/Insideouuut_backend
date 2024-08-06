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

	public static ChatUserResponse of(User host, ImageResponse profileImage) {
		ChatUserResponse hostResponse = new ChatUserResponse();

		hostResponse.id = host.getId();
		hostResponse.nickname = host.getNickname();
		hostResponse.profileImage = profileImage;

		return hostResponse;
	}
}
