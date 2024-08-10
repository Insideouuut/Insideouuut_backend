package com.goorm.insideout.meeting.domain;

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
public class MeetingQuestionAnswer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "question_answer_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "apply_id")
	private MeetingApply meetingApply;

	@Column(name = "question")
	private String question;

	@Column(name = "answer")
	private String answer;

	private MeetingQuestionAnswer(MeetingApply meetingApply, String question, String answer) {
		this.meetingApply = meetingApply;
		this.question = question;
		this.answer = answer;
	}

	public static MeetingQuestionAnswer of(MeetingApply meetingApply, String question, String answer) {
		return new MeetingQuestionAnswer(meetingApply, question, answer);
	}
}
