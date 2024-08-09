package com.goorm.insideout.club.entity;

import java.util.Arrays;

import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {

	SOCIAL("사교/취미"),
	SPORTS("운동"),
	STUDY("스터디");

	private final String name;

	public static Category findByName(String name) {
		return Arrays.stream(Category.values())
			.filter(c -> c.getName().equals(name))
			.findFirst()
			.orElseThrow(() -> ModongException.from(ErrorCode.CATEGORY_NOT_FOUND));
	}
}