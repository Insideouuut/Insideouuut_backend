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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;

@Entity
@Getter
public class Meeting {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "meeting_id")
	private Long id;

	@Column(name = "participants_number")
	private int participantsNumber;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "view", columnDefinition = "integer default 0", nullable = false)
	private int view;

	@Column(name = "rule")
	private String rule;

	@Column(name = "join_question")
	private String joinQuestion;

	@Column(name = "area")
	private String area;

	@Column(name = "date_time")
	private LocalDateTime dateTime;

	@Enumerated(EnumType.STRING)
	@Column(name = "level")
	private Level level;

	@Column(name = "minimum_age")
	private int minimumAge;

	@Column(name = "maximum_age")
	private int maximumAge;

	@Column(name = "male_ratio")
	private int maleRatio;

	@Column(name = "femail_ratio")
	private int femaleRatio;

	@Column(name = "has_membership_fee")
	private boolean hasMembershipFee;

	@Column(name = "membership_fee")
	private int membershipFee;

	@Column(name = "category")
	private String category;

	@Column(name = "gender_condition")
	private String genderCondition;

	@Column(name = "hobby")
	private String hobby;

	@OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
	private List<Image> images = new ArrayList<>();

	@OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
	private List<MeetingLike> likes = new ArrayList<>();

	// User 엔티티를 구현하지 않았으므로 임시 주석 처리
	// @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	// private User author;
}
