package com.goorm.insideout.club.dto.requestDto;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.club.entity.Category;
import com.goorm.insideout.club.entity.Level;
import com.goorm.insideout.club.entity.GenderRatio;

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
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private String date;
	private Integer participantLimit;
	private String hasGenderRatio;
	private String ratio;
	private int minAge;
	private int maxAge;
	private String name;
	private String introduction;
	private Set<String> rules;
	private List<String> joinQuestions;
	private String activityRegion;

	public Club setEnum(Club club) {
		int maleRatio = 0;
		int femaleRatio = 0;

		if (hasGenderRatio.equals("지정")) {
			String[] tokens = ratio.split(" : ");
			maleRatio = Integer.parseInt(tokens[0]);
			femaleRatio = Integer.parseInt(tokens[1]);
		}

		club.setCategory(Category.valueOf(category));
		club.setLevel(Level.valueOf(level));
		club.setGenderRatio(GenderRatio.valueOf(maleRatio, femaleRatio));

		return club;
	}

}
