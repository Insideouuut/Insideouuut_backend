package com.goorm.insideout.club.dto.responseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClubQuestionAnswerResponseDto {
	private Long applyId;
	private String question;
	private String answer;
}
