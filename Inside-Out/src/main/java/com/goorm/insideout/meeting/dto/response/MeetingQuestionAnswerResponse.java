package com.goorm.insideout.meeting.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeetingQuestionAnswerResponse {
	private Long applyId;
	private String question;
	private String answer;
}
