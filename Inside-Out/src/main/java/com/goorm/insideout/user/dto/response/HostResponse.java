package com.goorm.insideout.user.dto.response;

import com.goorm.insideout.image.dto.response.ImageResponse;
import com.goorm.insideout.user.domain.User;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class HostResponse {
	private Long id;
	private String nickname;
	private ImageResponse profileImage;

	public static HostResponse of(User host) {
		HostResponse hostResponse = new HostResponse();

		hostResponse.id = host.getId();
		hostResponse.nickname = host.getNickname();
		hostResponse.profileImage = ImageResponse.from(host.getProfileImage().getImage());

		return hostResponse;
	}
}
