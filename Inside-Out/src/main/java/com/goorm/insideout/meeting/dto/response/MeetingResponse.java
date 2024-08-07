package com.goorm.insideout.meeting.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.goorm.insideout.image.dto.response.ImageResponse;
import com.goorm.insideout.meeting.domain.Meeting;
import com.goorm.insideout.meeting.domain.MeetingPlace;
import com.goorm.insideout.user.dto.response.HostResponse;
import com.querydsl.core.annotations.QueryProjection;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MeetingResponse {
	private String title;
	private String description;
	private String rule;
	private int view;
	private int like;
	private boolean hasMembershipFee;
	private int membershipFee;
	private String progress;
	private String level;
	private String categoryDetail;
	private String category;
	private LocalDateTime schedule;
	private int participantsNumber;
	private int participantLimit;
	private int maleRatio;
	private int femaleRatio;
	private int minimumAge;
	private int maximumAge;
	private String joinQuestion;
	private HostResponse host;
	private MeetingPlaceResponse place;
	private List<ImageResponse> images;

	@QueryProjection
	public MeetingResponse(Meeting meeting) {
		this.title = meeting.getTitle();
		this.description = meeting.getDescription();
		this.rule = meeting.getRule();
		this.joinQuestion = meeting.getJoinQuestion();
		this.view = meeting.getView();
		this.like = meeting.getLikes().size();
		this.hasMembershipFee = meeting.isHasMembershipFee();
		this.membershipFee = meeting.getMembershipFee();
		this.progress = meeting.getProgress().name();
		this.level = meeting.getLevel().name();
		this.categoryDetail = meeting.getCategoryDetail();
		this.category = meeting.getCategory().name();
		this.schedule = meeting.getSchedule();
		this.participantsNumber = meeting.getParticipantsNumber();
		this.participantLimit = meeting.getParticipantLimit();
		this.maleRatio = meeting.getGenderRatio().getMaleRatio();
		this.femaleRatio = meeting.getGenderRatio().getFemaleRatio();
		this.minimumAge = meeting.getMinimumAge();
		this.maximumAge = meeting.getMaximumAge();
		this.host = HostResponse.of(meeting.getHost());
		this.images = meeting.getImages().stream()
			.map(meetingImage -> ImageResponse.from(meetingImage.getImage()))
			.toList();
		this.place = MeetingPlaceResponse.from(meeting.getMeetingPlace());
	}

	public static MeetingResponse of(Meeting meeting) {
		return new MeetingResponse(meeting);
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class MeetingPlaceResponse {
		private String name;
		private String placeUrl;
		private Long kakaoMapId;
		private String addressName;
		private String roadAddressName;
		private Double latitude;
		private Double longitude;

		private static MeetingPlaceResponse from(MeetingPlace meetingPlace) {
			MeetingPlaceResponse meetingPlaceResponse = new MeetingPlaceResponse();

			meetingPlaceResponse.name = meetingPlace.getName();
			meetingPlaceResponse.placeUrl = meetingPlace.getPlaceUrl();
			meetingPlaceResponse.kakaoMapId = meetingPlace.getKakaoMapId();
			meetingPlaceResponse.addressName = meetingPlace.getAddressName();
			meetingPlaceResponse.roadAddressName = meetingPlace.getRoadAddressName();
			meetingPlaceResponse.latitude = meetingPlace.getLatitude();
			meetingPlaceResponse.longitude = meetingPlace.getLongitude();

			return meetingPlaceResponse;
		}
	}
}