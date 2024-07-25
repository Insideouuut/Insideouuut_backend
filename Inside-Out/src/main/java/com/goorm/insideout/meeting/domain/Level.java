package com.goorm.insideout.meeting.domain;

import lombok.Getter;

@Getter
public enum Level {

	BEGINNER("하", "해당 스포츠에 이제 막 입문했거나 경험이 적은 사용자에요."),
	INTERMEDIATE("중", "해당 스포츠에 대한 기본 기술을 숙지하고 있으며, 꾸준한 훈련을 통해 실력을 향상시키고 있는 사용자에요."),
	ADVANCED("상", "해당 스포츠에 대한 고급 기술과 탁월한 체력 및 전략을 갖추고 있는 사용자에요."),
	NONE("레벨 없음", "레벨이 존재하지 않습니다.");

	private final String name;
	private final String description;

	Level(String name, String description) {
		this.name = name;
		this.description = description;
	}
}
