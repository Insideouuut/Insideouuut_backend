package com.goorm.insideout.meeting.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.goorm.insideout.image.dto.response.ImageResponse;
import com.goorm.insideout.meeting.domain.Meeting;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MeetingResponse {
	private String title;
	// User 엔티티를 구현하지 않았으므로 임시 주석 처리
	// private UserResponse user;
	private String description;
	private List<ImageResponse> images;
	private String rule;
	private String joinQuestion;
	private int view;
	private int like;
	private boolean hasMembershipFee;
	private int membershipFee;
	private String level;
	private String hobby;
	private String category;
	private String location;
	private LocalDateTime schedule;
	private int participantsNumber;
	private int participantLimit;
	private String genderCondition;
	private int maleRatio;
	private int femaleRatio;
	private int minimumAge;
	private int maximumAge;

	@QueryProjection
	public MeetingResponse(Meeting meeting) {
		this.title = meeting.getTitle();
		// User 엔티티를 구현하지 않았으므로 임시 주석 처리
		// this.user = new UserResponse(meeting.getAuthor());
		this.description = meeting.getDescription();
		this.images = meeting.getImages()
			.stream()
			.map(ImageResponse::new)
			.toList();
		this.rule = meeting.getRule();
		this.joinQuestion = meeting.getJoinQuestion();
		this.view = meeting.getView();
		this.like = meeting.getLikes().size();
		this.hasMembershipFee = meeting.isHasMembershipFee();
		this.membershipFee = meeting.getMembershipFee();
		this.level = meeting.getLevel().getName();
		this.hobby = meeting.getHobby();
		this.category = meeting.getCategory().getName();
		this.location = meeting.getLocation();
		this.schedule = meeting.getSchedule();
		this.participantsNumber = meeting.getParticipantsNumber();
		this.participantLimit = meeting.getParticipantLimit();
		this.genderCondition = meeting.getGenderCondition();
		this.maleRatio = meeting.getMaleRatio();
		this.femaleRatio = meeting.getFemaleRatio();
		this.minimumAge = meeting.getMinimumAge();
		this.maximumAge = meeting.getMaximumAge();
	}

	public MeetingResponse from(Meeting meeting) {
		return new MeetingResponse(meeting);
	}
}