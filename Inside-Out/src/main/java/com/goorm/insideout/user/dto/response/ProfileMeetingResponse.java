package com.goorm.insideout.user.dto.response;

import com.goorm.insideout.meeting.dto.response.MeetingResponse;

import lombok.Getter;

@Getter
public class ProfileMeetingResponse {
	private final String category;
	private final String meetingName;
	private final String meetingImage;
	private final String meetingHostNickName;
	public ProfileMeetingResponse(MeetingResponse meetingResponse){
		this.category="MEETING";
		this.meetingName=meetingResponse.getName();
		this.meetingImage=meetingResponse.getImages().get(0).getUrl();
		this.meetingHostNickName=meetingResponse.getHost().getNickname();
	}

}
