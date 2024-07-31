package com.goorm.insideout.meeting.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.goorm.insideout.image.dto.response.ImageResponse;
import com.goorm.insideout.meeting.domain.Meeting;
import com.goorm.insideout.meeting.domain.MeetingPlace;
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
	private String progress;
	private String level;
	private String hobby;
	private String category;
	private MeetingPlaceResponse place;
	private LocalDateTime schedule;
	private int participantsNumber;
	private int participantLimit;
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
		this.progress = meeting.getProgress().name();
		this.level = meeting.getLevel().name();
		this.hobby = meeting.getHobby();
		this.category = meeting.getCategory().name();
		this.place = new MeetingPlaceResponse().toDto(meeting.getMeetingPlace());
		this.schedule = meeting.getSchedule();
		this.participantsNumber = meeting.getParticipantsNumber();
		this.participantLimit = meeting.getParticipantLimit();
		this.maleRatio = meeting.getGenderRatio().getMaleRatio();
		this.femaleRatio = meeting.getGenderRatio().getFemaleRatio();
		this.minimumAge = meeting.getMinimumAge();
		this.maximumAge = meeting.getMaximumAge();
	}

	public static MeetingResponse from(Meeting meeting) {
		return new MeetingResponse(meeting);
	}

	public static class MeetingPlaceResponse {
		private String name;

		private String placeUrl;

		private Long kakaoMapId;

		private Double latitude;

		private Double longitude;

		private MeetingPlaceResponse toDto(MeetingPlace meetingPlace) {
			this.name = meetingPlace.getName();
			this.placeUrl = meetingPlace.getPlaceUrl();
			this.kakaoMapId = meetingPlace.getKakaoMapId();
			this.latitude = meetingPlace.getLatitude();
			this.longitude = meetingPlace.getLongitude();

			return this;
		}
	}
}