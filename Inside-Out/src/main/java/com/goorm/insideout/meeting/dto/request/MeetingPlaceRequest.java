package com.goorm.insideout.meeting.dto.request;

import com.goorm.insideout.meeting.domain.MeetingPlace;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MeetingPlaceRequest {
	private String name;

	private String placeUrl;

	private Long kakaoMapId;

	private String addressName;

	private String roadAddressName;

	private Double latitude;

	private Double longitude;

	public MeetingPlace toEntity() {
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