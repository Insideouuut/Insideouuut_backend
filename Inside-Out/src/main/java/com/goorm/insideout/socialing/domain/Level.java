package com.goorm.insideout.socialing.domain;

public enum Level {

	STARTER("스타터", "스타터"),
	BEGINNER("비기너", "비기너"),
	AMATEUR("아마추어", "아마추어"),
	SEMI_PRO("세미프로", "세미프로"),
	PRO("프로", "프로");

	private final String name;
	private final String description;

	Level(String name, String description) {
		this.name = name;
		this.description = description;
	}
}
