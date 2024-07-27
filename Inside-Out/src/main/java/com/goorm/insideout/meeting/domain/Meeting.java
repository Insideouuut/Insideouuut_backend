package com.goorm.insideout.meeting.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.goorm.insideout.image.domain.Image;
import com.goorm.insideout.like.domain.MeetingLike;
import com.goorm.insideout.user.domain.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meeting {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "meeting_id")
	private Long id;

	@Column(name = "participants_number")
	private int participantsNumber;

	@Column(name = "participant_limit")
	private int participantLimit;

	@Lob
	@Column(name = "title")
	private String title;

	@Lob
	@Column(name = "description")
	private String description;

	@Column(name = "view", columnDefinition = "integer default 0", nullable = false)
	private int view;

	@Column(name = "rule")
	private String rule;

	@Column(name = "join_question")
	private String joinQuestion;

	@Column(name = "schedule")
	private LocalDateTime schedule;

	@Enumerated(EnumType.STRING)
	@Column(name = "level")
	private Level level;

	@Column(name = "minimum_age")
	private int minimumAge;

	@Column(name = "maximum_age")
	private int maximumAge;

	@Enumerated(EnumType.STRING)
	@Column(name = "gender_ratio")
	private GenderRatio genderRatio;

	@Column(name = "has_membership_fee")
	private boolean hasMembershipFee;

	@Column(name = "membership_fee")
	private int membershipFee;

	@Enumerated(EnumType.STRING)
	@Column(name = "category")
	private Category category;

	@Column(name = "hobby")
	private String hobby;

	@OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
	private List<Image> images = new ArrayList<>();

	@OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
	private List<MeetingLike> likes = new ArrayList<>();

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User host;

	@OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
	private List<MeetingUser> meetingUsers = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "place_id")
	private Place place;

	/*
	 * 연관관계 설정 메서드
	 */
	private void setHost(User host) {
		this.host = host;
		host.getMeetings().add(this);
	}

	private void setPlace(Place place) {
		this.place = place;
		place.getMeetings().add(this);
	}
}
