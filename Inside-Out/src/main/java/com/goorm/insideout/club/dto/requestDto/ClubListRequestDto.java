package com.goorm.insideout.club.dto.requestDto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ClubListRequestDto {
	private String clubName;

	private String category;

	private String date;

	private String region;

	private Integer memberLimit;
	//Integer memberCunt;
	Integer price;
	private Integer ageLimit;

	Boolean isRecruiting = false;
}
