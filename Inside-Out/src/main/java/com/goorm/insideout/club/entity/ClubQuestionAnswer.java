package com.goorm.insideout.club.entity;

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
public class ClubQuestionAnswer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "question_answer_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "apply_id")
	private ClubApply clubApply;

	@Column(name = "question")
	private String question;

	@Column(name = "answer")
	private String answer;

	private ClubQuestionAnswer(ClubApply clubApply, String question, String answer) {
		this.clubApply = clubApply;
		this.question = question;
		this.answer = answer;
	}

	public static ClubQuestionAnswer of(ClubApply clubApply, String question, String answer) {
		return new ClubQuestionAnswer(clubApply, question, answer);
	}
}
