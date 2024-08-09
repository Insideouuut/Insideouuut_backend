package com.goorm.insideout.club.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {

	SOCIAL("사교/취미"),
	SPORTS("운동"),
	STUDY("스터디");

	private final String name;
}
