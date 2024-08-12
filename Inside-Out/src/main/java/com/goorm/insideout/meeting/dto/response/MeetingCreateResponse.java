package com.goorm.insideout.meeting.dto.response;

import com.goorm.insideout.meeting.domain.Meeting;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingCreateResponse {
	private Long meetingId;

	public static MeetingCreateResponse of(Meeting meeting) {
		MeetingCreateResponse response = new MeetingCreateResponse();

		response.meetingId = meeting.getId();

		return response;
	}
}