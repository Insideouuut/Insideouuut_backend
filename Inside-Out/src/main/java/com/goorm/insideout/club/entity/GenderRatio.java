package com.goorm.insideout.club.entity;

import java.util.Arrays;

import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenderRatio {

	ONLY_MALE(10, 0), // 남자만
	ONLY_FEMALE(0, 10), // 여자만

	ONE_TO_NINE(1, 9),
	TWO_TO_EIGHT(2, 8),
	THREE_TO_SEVEN(3, 7),
	FOUR_TO_SIX(4, 6),
	FIVE_TO_FIVE(5, 5),
	SIX_TO_FOUR(6, 4),
	SEVEN_TO_THREE(7, 3),
	EIGHT_TO_TWO(8, 2),
	NINE_TO_ONE(9, 1),

	IRRELEVANT(0, 0); // 성별 비율 무관

	private final int maleRatio;
	private final int femaleRatio;

	public static com.goorm.insideout.club.entity.GenderRatio valueOf(int maleRatio, int femaleRatio) {
		return Arrays.stream(com.goorm.insideout.club.entity.GenderRatio.values())
			.filter(genderRatio -> genderRatio.maleRatio == maleRatio && genderRatio.femaleRatio == femaleRatio)
			.findFirst()
			.orElseThrow(() -> ModongException.from(ErrorCode.MEETING_GENDER_RATIO_INVALID));
	}
}