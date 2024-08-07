package com.goorm.insideout.meeting.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.goorm.insideout.image.dto.response.ImageResponse;
import com.goorm.insideout.meeting.domain.Category;
import com.goorm.insideout.meeting.domain.Level;
import com.goorm.insideout.meeting.domain.Meeting;
import com.goorm.insideout.meeting.domain.MeetingPlace;
import com.goorm.insideout.meeting.domain.Progress;
import com.goorm.insideout.user.dto.response.HostResponse;
import com.querydsl.core.annotations.QueryProjection;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MeetingResponse {
	private String name;
	private String introduction;
	private int view;
	private int like;
	private boolean hasMembershipFee;
	private int membershipFeeAmount;
	private String progress;
	private String level;
	private String categoryDetail;
	private String category;
	@JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss")
	private LocalDateTime date;
	private int participantsNumber;
	private int participantLimit;
	private String ratio;
	private List<Integer> ageRange;
	private Set<String> rules;
	private Set<String> joinQuestions;
	private HostResponse host;
	private MeetingPlaceResponse place;
	private List<ImageResponse> images;

	@QueryProjection
	public MeetingResponse(Meeting meeting) {
		this.name = meeting.getTitle();
		this.introduction = meeting.getDescription();
		this.rules = meeting.getRules();
		this.joinQuestions = meeting.getJoinQuestions();
		this.view = meeting.getView();
		this.like = meeting.getLikes().size();
		this.hasMembershipFee = meeting.isHasMembershipFee();
		this.membershipFeeAmount = meeting.getMembershipFee();
		this.progress = meeting.getProgress().name();
		this.level = meeting.getLevel().getName();
		this.categoryDetail = meeting.getCategoryDetail();
		this.category = meeting.getCategory().getName();
		this.date = meeting.getSchedule();
		this.participantsNumber = meeting.getParticipantsNumber();
		this.participantLimit = meeting.getParticipantLimit();
		this.ratio = meeting.getGenderRatio().getMaleRatio() + " : " + meeting.getGenderRatio().getFemaleRatio();
		this.ageRange = List.of(meeting.getMinimumAge(), meeting.getMaximumAge());
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