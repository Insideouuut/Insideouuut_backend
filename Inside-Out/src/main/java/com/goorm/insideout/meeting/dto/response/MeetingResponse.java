package com.goorm.insideout.meeting.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.goorm.insideout.image.domain.Image;
import com.goorm.insideout.image.dto.response.ImageResponse;
import com.goorm.insideout.meeting.domain.Meeting;
import com.goorm.insideout.user.dto.UserResponse;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MeetingResponse {
	private String name;
	private UserResponse user;
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
	private String area;
	private LocalDateTime dateTime;
	private int participantsNumber;
	private String genderCondition;
	private int maleRatio;
	private int femaleRatio;
	private int minimumAge;
	private int maximumAge;

	@QueryProjection
	public MeetingResponse(Meeting meeting) {
		this.name = meeting.getName();
		this.user = new UserResponse(meeting.getAuthor());
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
		this.category = meeting.getCategory();
		this.area = meeting.getArea();
		this.dateTime = meeting.getDateTime();
		this.participantsNumber = meeting.getParticipantsNumber();
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