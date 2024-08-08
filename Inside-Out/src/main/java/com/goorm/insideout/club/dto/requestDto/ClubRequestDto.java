package com.goorm.insideout.club.dto.requestDto;

import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ClubRequestDto {


	private String category;
	private String categoryDetail;
	private String level;
	private boolean hasMembershipFee;
	private Integer membershipFeeAmount;
	private String date;
	private Integer participantLimit;
	private String hasGenderRatio;
	private String ratio;
	private List<Integer> ageRange;
	private String name;
	private String introduction;
	private Set<String> rules;
	private Set<String> joinQuestions;
	private String activityRegion;

}
