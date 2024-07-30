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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "host_id")
	private User host;

	@OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
	private List<MeetingUser> meetingUsers = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "place_id")
	private Place place;

	/**
	 * 생성 메서드
	 */
	public static Meeting createMeeting (
		String title,
		String description,
		Category category,
		int participantsNumber,
		int participantLimit,
		String rule,
		String joinQuestion,
		LocalDateTime schedule,
		Level level,
		int minimumAge,
		int maximumAge,
		GenderRatio genderRatio,
		boolean hasMembershipFee,
		int membershipFee,
		String hobby,
		User host,
		Place place
	) {
		Meeting meeting = new Meeting();

		meeting.title = title;
		meeting.description = description;
		meeting.category = category;
		meeting.participantsNumber = participantsNumber;
		meeting.participantLimit = participantLimit;
		meeting.rule = rule;
		meeting.joinQuestion = joinQuestion;
		meeting.schedule = schedule;
		meeting.level = level;
		meeting.minimumAge = minimumAge;
		meeting.maximumAge = maximumAge;
		meeting.genderRatio = genderRatio;
		meeting.hasMembershipFee = hasMembershipFee;
		meeting.membershipFee = membershipFee;
		meeting.hobby = hobby;
		meeting.setHost(host);
		meeting.setPlace(place);

		return meeting;
	}

	/**
	 * 수정 메서드
	 */
	public void updateMeeting(Meeting meeting) {
		this.title = meeting.title;
		this.description = meeting.description;
		this.category = meeting.category;
		this.participantsNumber = meeting.participantsNumber;
		this.participantLimit = meeting.participantLimit;
		this.rule = meeting.rule;
		this.joinQuestion = meeting.joinQuestion;
		this.schedule = meeting.schedule;
		this.level = meeting.level;
		this.minimumAge = meeting.minimumAge;
		this.maximumAge = meeting.maximumAge;
		this.genderRatio = meeting.genderRatio;
		this.hasMembershipFee = meeting.hasMembershipFee;
		this.membershipFee = meeting.membershipFee;
		this.hobby = meeting.hobby;
		this.setMeetingPlace(meeting.getMeetingPlace());
	}

	/**
	 * 비즈니스 로직
	 */
	public boolean isHost(User user) {
		return this.host.equals(user);
	}

	public void changeProgress(Progress progress) {
		this.progress = progress;
	}

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
