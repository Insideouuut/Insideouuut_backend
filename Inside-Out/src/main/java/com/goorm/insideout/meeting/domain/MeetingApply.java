package com.goorm.insideout.meeting.domain;

import com.goorm.insideout.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingApply {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "apply_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "meeting_id")
	private Meeting meeting;

	private MeetingApply(User user, Meeting meeting) {
		this.user = user;
		this.meeting = meeting;
	}

	public static MeetingApply of(User user, Meeting meeting) {
		return new MeetingApply(user, meeting);
	}
}

