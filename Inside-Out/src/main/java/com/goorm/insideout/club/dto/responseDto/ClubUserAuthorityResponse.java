package com.goorm.insideout.club.dto.responseDto;

import com.goorm.insideout.meeting.domain.Role;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubUserAuthorityResponse {
	private String authority;
	private String message;

	public static ClubUserAuthorityResponse of(Role role) {
		ClubUserAuthorityResponse response = new ClubUserAuthorityResponse();

		response.authority = role.getName();
		response.message = getMessageByRole(role);

		return response;
	}

	private static String getMessageByRole(Role role) {
		if (role == Role.HOST) {
			return "해당 동아리의 호스트입니다.";
		}
		if (role == Role.MEMBER) {
			return "해당 동아리의 일반 멤버입니다.";
		} else {
			return "해당 동아리에 권한이 없는 사용자입니다.";
		}
	}
}
