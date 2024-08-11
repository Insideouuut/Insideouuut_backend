package com.goorm.insideout.meeting.domain;

import java.util.Arrays;

import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Type {
	MEETING("모임"),
	CLUB_MEETING("동아리 모임");

	private final String name;

	public static Type findByName(String name) {
		return Arrays.stream(Type.values())
			.filter(type -> type.getName().equals(name))
			.findAny()
			.orElseThrow(() -> ModongException.from(ErrorCode.MEETING_TYPE_NOT_FOUND));
	}
}
