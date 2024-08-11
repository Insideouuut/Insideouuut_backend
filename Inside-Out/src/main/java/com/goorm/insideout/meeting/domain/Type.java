package com.goorm.insideout.meeting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Type {
	MEETING("모임"),
	CLUB_MEETING("동아리 모임");

	private final String name;
}
