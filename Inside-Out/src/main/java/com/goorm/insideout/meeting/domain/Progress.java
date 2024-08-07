package com.goorm.insideout.meeting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Progress {
	ONGOING("진행중"),
	ENDED("종료");

	private final String name;
}
