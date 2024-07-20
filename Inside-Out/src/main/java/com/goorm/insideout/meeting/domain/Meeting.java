package com.goorm.insideout.meeting.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.goorm.insideout.image.domain.Image;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

	@Column(name = "rule")
	private String rule;

	@Column(name = "join_question")
	private String joinQuestion;

	@Column(name = "area")
	private String area;

	@Column(name = "date_time")
	private LocalDateTime dateTime;

	@Column(name = "minimum_age")
	private int minimumAge;

	@Column(name = "maximum_age")
	private int maximumAge;

	@Column(name = "male_ratio")
	private int maleRatio;

	@Column(name = "femail_ratio")
	private long femaleRatio;

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
}
