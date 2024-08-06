package com.goorm.insideout.user.dto.response;

import com.goorm.insideout.user.domain.User;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class HostResponse {
	private Long id;
	private String nickname;
	private String profileImage;

	public static HostResponse fromEntity(User host) {
		HostResponse hostResponse = new HostResponse();

		hostResponse.id = host.getId();
		hostResponse.nickname = host.getNickname();
		hostResponse.profileImage = host.getProfileImage();

		return hostResponse;
	}
}
