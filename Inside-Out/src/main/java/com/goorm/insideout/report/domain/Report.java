package com.goorm.insideout.report.domain;

import com.goorm.insideout.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class Report {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String reason;

	@ManyToOne
	@JoinColumn(name = "reported_by_user_id", nullable = false)
	private User reportedBy;

	@ManyToOne
	@JoinColumn(name = "reported_user_id", nullable = false)
	private User reportedUser;

	public Report(String reason, User reportedBy, User reportedUser) {
		this.reason = reason;
		this.reportedBy = reportedBy;
		this.reportedUser = reportedUser;
	}
}
