package com.goorm.insideout.meeting.dto.request;

import java.util.List;

import com.goorm.insideout.meeting.dto.AnswerDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeetingApplyRequest {
	private List<AnswerDto> answers;
}
