package com.goorm.insideout.image.domain;

import com.goorm.insideout.club.entity.Club;

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
public class ClubImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "club_image_id")
	private Long id;

	@Embedded
	private Image image;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "club_id")
	private Club club;

	/**
	 * 생성 메서드
	 */
	public static ClubImage createClubImage(
		String uploadName,
		String storeName,
		String url,
		Club club
	) {
		ClubImage clubImage = new ClubImage();

		clubImage.image = Image.of(uploadName, storeName, url);
		clubImage.setClub(club);

		return clubImage;
	}

	/**
	 * 연관관계 설정 메서드
	 */
	public void setClub(Club club) {
		this.club = club;
		club.getImages().add(this);
	}
}
