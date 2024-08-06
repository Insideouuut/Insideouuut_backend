package com.goorm.insideout.image.domain;

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
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
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

	/**
	 * 생성 메서드
	 */
	public static MeetingImage createMeetingImage(
		String uploadName,
		String storeName,
		Meeting meeting
	) {
		MeetingImage meetingImage = new MeetingImage();

		meetingImage.image = new Image(uploadName, storeName);
		meetingImage.setMeeting(meeting);

		return meetingImage;
	}

	/**
	 * 연관관계 설정 메서드
	 */
	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
		meeting.getImages().add(this);
	}
}
