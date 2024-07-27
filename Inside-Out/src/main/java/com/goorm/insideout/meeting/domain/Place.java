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
public class Place {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "place_id")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "place_url")
	private String placeUrl;

	@Column(name = "map_id")
	private Long mapId;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "longitude")
	private Double longitude;

	@OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
	private List<Meeting> meetings = new ArrayList<>();

	/**
	 * 생성 메서드
	 */
	public static Place createPlace(
		String name,
		String placeUrl,
		Long mapId,
		Double latitude,
		Double longitude
	) {
		Place place = new Place();

		place.name = name;
		place.placeUrl = placeUrl;
		place.mapId = mapId;
		place.latitude = latitude;
		place.longitude = longitude;

		return place;
	}
}
