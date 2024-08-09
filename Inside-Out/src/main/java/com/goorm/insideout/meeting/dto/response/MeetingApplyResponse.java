package com.goorm.insideout.meeting.dto.response;

import com.goorm.insideout.meeting.domain.MeetingApply;
import com.goorm.insideout.user.dto.response.BasicUserResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MeetingApplyResponse {

	BasicUserResponse basicUserResponse;
	private Long applyId;
	private String answer;
	public static MeetingApplyResponse of(MeetingApply meetingApply) {
		MeetingApplyResponse response = new MeetingApplyResponse();
		response.basicUserResponse = BasicUserResponse.from(meetingApply.getUser());
		response.applyId = meetingApply.getId();
		response.answer = meetingApply.getAnswer();

		return response;
	}
}
