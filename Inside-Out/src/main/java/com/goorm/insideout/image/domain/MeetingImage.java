package com.goorm.insideout.image.domain;

import java.util.IdentityHashMap;

import com.goorm.insideout.meeting.domain.Meeting;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class MeetingImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "meeting_image_id")
	private Long id;

	@Embedded
	private Image image;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "meeting_id")
	private Meeting meeting;

	public MeetingImage(String uploadName, String storeName) {
		this.image = new Image(uploadName, storeName);
	}

	/**
	 * 연관관계 설정 메서드
	 */
	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}
}
