package com.goorm.insideout.meeting.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.goorm.insideout.image.domain.MeetingImage;
import com.goorm.insideout.like.domain.MeetingLike;
import com.goorm.insideout.user.domain.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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

	@Lob
	@Column(name = "title", nullable = false)
	private String title;

	@Lob
	@Column(name = "description", nullable = false)
	private String description;

	@ElementCollection
	@CollectionTable(name = "meeting_rules", joinColumns = @JoinColumn(name = "meeting_id"))
	@Column(name = "rule")
	private Set<String> rules;

	@ElementCollection
	@CollectionTable(name = "meeting_join_questions", joinColumns = @JoinColumn(name = "meeting_id"))
	@Column(name = "join_question")
	private Set<String> joinQuestions;

	@Column(name = "schedule", nullable = false)
	private LocalDateTime schedule;

	@Enumerated(EnumType.STRING)
	@Column(name="progress", nullable = false)
	private Progress progress;

	@Enumerated(EnumType.STRING)
	@Column(name = "level", nullable = false)
	private Level level;

	@Column(name = "participants_number", columnDefinition = "integer default 0", nullable = false)
	private int participantsNumber;

	@Column(name = "participant_limit", nullable = false)
	private int participantLimit;

	@Column(name = "minimum_age", nullable = false)
	private int minimumAge;

	@Column(name = "maximum_age", nullable = false)
	private int maximumAge;

	@Enumerated(EnumType.STRING)
	@Column(name = "gender_ratio", nullable = false)
	private GenderRatio genderRatio;

	@Column(name = "has_membership_fee", nullable = false)
	private boolean hasMembershipFee;

	@Column(name = "membership_fee", nullable = false)
	private int membershipFee;

	@Enumerated(EnumType.STRING)
	@Column(name = "category", nullable = false)
	private Category category;

	@Column(name = "category_detail", nullable = false)
	private String categoryDetail;

	@Column(name = "view", columnDefinition = "integer default 0", nullable = false)
	private int view;

	@OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
	private List<MeetingImage> images = new ArrayList<>();

	@OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
	private List<MeetingLike> likes = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "host_id")
	private User host;

	@OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
	private List<MeetingUser> meetingUsers = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "meeting_place_id")
	private MeetingPlace meetingPlace;

	/**
	 * 생성 메서드
	 */
	public static Meeting createMeeting (
		String title,
		String description,
		Category category,
		String categoryDetail,
		int participantLimit,
		Set<String> joinQuestions,
		LocalDateTime schedule,
		Level level,
		int minimumAge,
		int maximumAge,
		GenderRatio genderRatio,
		boolean hasMembershipFee,
		int membershipFee,
		User user,
		MeetingPlace meetingPlace,
		Set<String> rules
	) {
		Meeting meeting = new Meeting();

		meeting.title = title;
		meeting.description = description;
		meeting.category = category;
		meeting.categoryDetail = categoryDetail;
		meeting.participantsNumber = 1;
		meeting.participantLimit = participantLimit;
		meeting.rules = rules;
		meeting.joinQuestions = joinQuestions;
		meeting.schedule = schedule;
		meeting.progress = Progress.ONGOING;
		meeting.level = level;
		meeting.minimumAge = minimumAge;
		meeting.maximumAge = maximumAge;
		meeting.genderRatio = genderRatio;
		meeting.hasMembershipFee = hasMembershipFee;
		meeting.membershipFee = membershipFee;
		meeting.host = user;
		meeting.meetingPlace = meetingPlace;

		return meeting;
	}

	/**
	 * 수정 메서드
	 */
	public void updateMeeting(Meeting meeting) {
		this.title = meeting.title;
		this.description = meeting.description;
		this.category = meeting.category;
		this.categoryDetail = meeting.categoryDetail;
		this.participantsNumber = meeting.participantsNumber;
		this.participantLimit = meeting.participantLimit;
		this.joinQuestions = meeting.joinQuestions;
		this.schedule = meeting.schedule;
		this.level = meeting.level;
		this.minimumAge = meeting.minimumAge;
		this.maximumAge = meeting.maximumAge;
		this.genderRatio = meeting.genderRatio;
		this.hasMembershipFee = meeting.hasMembershipFee;
		this.membershipFee = meeting.membershipFee;
		this.meetingPlace = meeting.meetingPlace;
		this.rules = meeting.rules;
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
}
