package com.goorm.insideout.meeting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApprovalStatus {

	APPROVED("승인"),
	DENIED("거절"),
	PENDING("대기");

	private final String name;
}
