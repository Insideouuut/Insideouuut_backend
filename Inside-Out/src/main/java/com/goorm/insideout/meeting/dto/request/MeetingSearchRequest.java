package com.goorm.insideout.meeting.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetingSearchRequest {
	private String query;
	private String category;
	private String sortType;
}
