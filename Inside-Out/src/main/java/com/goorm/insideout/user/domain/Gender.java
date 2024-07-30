package com.goorm.insideout.user.domain;

import static com.goorm.insideout.global.exception.ErrorCode.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.goorm.insideout.global.exception.ModongException;

import lombok.Getter;

@Getter
public enum Gender {
	MALE("남성"),
	FEMALE("여성");

	private final String name;

	Gender(String name) {
		this.name = name;
	}
}
