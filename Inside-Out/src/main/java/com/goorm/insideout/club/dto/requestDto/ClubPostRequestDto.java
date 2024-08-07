package com.goorm.insideout.club.dto.requestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ClubPostRequestDto {

	private String postTitle;
	private String category;
	private String postContent;
}
