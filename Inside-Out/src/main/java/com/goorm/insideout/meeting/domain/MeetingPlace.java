package com.goorm.insideout.meeting.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingPlace {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "meeting_place_id")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "place_url")
	private String placeUrl;

	@Column(name = "map_id")
	private Long kakaoMapId;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "longitude")
	private Double longitude;

	@OneToMany(mappedBy = "meetingPlace", cascade = CascadeType.ALL)
	private List<Meeting> meetings = new ArrayList<>();

	/**
	 * 생성 메서드
	 */
	public static MeetingPlace createMeetingPlace(
		String name,
		String placeUrl,
		Long mapId,
		Double latitude,
		Double longitude
	) {
		MeetingPlace meetingPlace = new MeetingPlace();

		meetingPlace.name = name;
		meetingPlace.placeUrl = placeUrl;
		meetingPlace.kakaoMapId = mapId;
		meetingPlace.latitude = latitude;
		meetingPlace.longitude = longitude;

		return meetingPlace;
	}
}
