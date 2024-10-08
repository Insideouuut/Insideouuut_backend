package com.goorm.insideout.meeting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

	HOST("호스트"),
	MEMBER("멤버"),
	NONE("권한 없음");

	private final String name;
}
