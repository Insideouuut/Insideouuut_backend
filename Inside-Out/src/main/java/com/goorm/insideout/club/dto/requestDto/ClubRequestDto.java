package com.goorm.insideout.club.dto.requestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ClubRequestDto {

	private String clubName;

	private String category;

	//private LocalDateTime createdAt;

	private String content;

	private String date;

	private String region;
	private String question;


	private Integer memberLimit;
	private Integer price;
	private Integer ageLimit;
}
