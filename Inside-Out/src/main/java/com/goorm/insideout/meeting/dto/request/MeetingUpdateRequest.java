package com.goorm.insideout.meeting.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
	private String name;

	private String introduction;

	private String category;

	private String categoryDetail;

	private MeetingCreateRequest.MeetingPlaceRequest meetingPlace;

	private int participantLimit;

	private Set<String> rules;

	private Set<String> joinQuestions;

	private LocalDateTime date;

	private String level;

	private List<Integer> ageRange;

	private String hasGenderRatio;

	private String ratio;

	private boolean hasMembershipFee;

	private int membershipFeeAmount;

	public Meeting toEntity(User host, MeetingPlace meetingPlace) {
		int maleRatio = 0;
		int femaleRatio = 0;

		if (hasGenderRatio.equals("지정")) {
			String[] tokens = ratio.split(" : ");
			maleRatio = Integer.parseInt(tokens[0]);
			femaleRatio = Integer.parseInt(tokens[1]);
		}

		return Meeting.createMeeting(
			name,
			introduction,
			Category.valueOf(category),
			categoryDetail,
			participantLimit,
			joinQuestions,
			date,
			Level.valueOf(level),
			ageRange.get(0),
			ageRange.get(1),
			GenderRatio.valueOf(maleRatio, femaleRatio),
			hasMembershipFee,
			membershipFeeAmount,
			host,
			meetingPlace,
			rules
		);
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	private static class MeetingPlaceRequest {
		private String name;

		private String placeUrl;

		private Long kakaoMapId;

		private String addressName;

		private String roadAddressName;

		private Double latitude;

		private Double longitude;

		private MeetingPlace toEntity() {
			return MeetingPlace.createMeetingPlace(
				name,
				placeUrl,
				kakaoMapId,
				addressName,
				roadAddressName,
				latitude,
				longitude
			);
		}
	}
}
