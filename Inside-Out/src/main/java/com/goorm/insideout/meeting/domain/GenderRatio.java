package com.goorm.insideout.meeting.domain;

import java.util.Arrays;

import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenderRatio {

	ONLY_MALE(100, 0), // 남자만
	ONLY_FEMALE(0, 100), // 여자만

	ONE_TO_NINE(10, 90),
	TWO_TO_EIGHT(20, 80),
	THREE_TO_SEVEN(30, 70),
	FOUR_TO_SIX(40, 60),
	FIVE_TO_FIVE(50, 50),
	SIX_TO_FOUR(60, 40),
	SEVEN_TO_THREE(70, 30),
	EIGHT_TO_TWO(80, 20),
	NINE_TO_ONE(90, 10),

	IRRELEVANT(0, 0); // 성별 비율 무관

	private final int maleRatio;
	private final int femaleRatio;

	public static GenderRatio valueOf(int maleRatio, int femaleRatio) {
		return Arrays.stream(GenderRatio.values())
			.filter(genderRatio -> genderRatio.maleRatio == maleRatio && genderRatio.femaleRatio == femaleRatio)
			.findFirst()
			.orElseThrow(() -> ModongException.from(ErrorCode.MEETING_GENDER_RATIO_INVALID));
	}
}
