package com.goorm.insideout.club.dto.requestDto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ClubRequestDto {

	private String clubName;

	private String category;

	//private LocalDateTime createdAt;

	private String content;

	private LocalDateTime date;

	private String region;
	private String question;


	private Integer memberLimit;
	private Integer price;
	private Integer ageLimit;
}
