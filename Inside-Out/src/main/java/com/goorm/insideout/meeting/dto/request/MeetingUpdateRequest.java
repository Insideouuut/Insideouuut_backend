package com.goorm.insideout.meeting.dto.request;

import java.time.LocalDateTime;

import com.goorm.insideout.meeting.domain.Category;
import com.goorm.insideout.meeting.domain.GenderRatio;
import com.goorm.insideout.meeting.domain.Level;
import com.goorm.insideout.meeting.domain.Meeting;
import com.goorm.insideout.meeting.domain.MeetingPlace;
import com.goorm.insideout.user.domain.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class MeetingUpdateRequest {
	private String title;

	private String description;

	private String category;

	private MeetingPlaceRequest meetingPlace;

	private int participantLimit;

	private String rule;

	private String joinQuestion;

	private LocalDateTime schedule;

	private String level;

	private int minimumAge;

	private int maximumAge;

	private int maleRatio;

	private int femaleRatio;

	private boolean hasMembershipFee;

	private int membershipFee;

	private String hobby;

	public Meeting toEntity(User host) {
		return Meeting.createMeeting(
			title,
			description,
			Category.valueOf(category),
			participantLimit,
			rule,
			joinQuestion,
			schedule,
			Level.valueOf(level),
			minimumAge,
			maximumAge,
			GenderRatio.valueOf(maleRatio, femaleRatio),
			hasMembershipFee,
			membershipFee,
			hobby,
			host,
			meetingPlace.toEntity()
		);
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	private static class MeetingPlaceRequest {
		private String name;

		private String placeUrl;

		private Long kakaoMapId;

		private Double latitude;

		private Double longitude;

		private MeetingPlace toEntity() {
			return MeetingPlace.createMeetingPlace(
				name,
				placeUrl,
				kakaoMapId,
				latitude,
				longitude
			);
		}
	}
}