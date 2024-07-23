package com.goorm.insideout.meeting.domain;

import lombok.Getter;

@Getter
public enum Category {

	SOCIAL("사교/취미"),
	SPORTS("운동"),
	STUDY("스터디");

	private final String name;

	Category(String name) {
		this.name = name;
	}
}
