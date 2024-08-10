package com.goorm.insideout.image.domain;

import com.goorm.insideout.club.entity.ClubPost;

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
public class ClubPostImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "club_post_image_id")
	private Long id;

	@Embedded
	private Image image;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "club_post_id")
	private ClubPost clubPost;

	/**
	 * 생성 메서드
	 */
	public static ClubPostImage createClubPostImage(
		String uploadName,
		String storeName,
		String url,
		ClubPost clubPost
	) {
		ClubPostImage clubPostImage = new ClubPostImage();

		clubPostImage.image = Image.of(uploadName, storeName, url);
		clubPostImage.setClubPost(clubPost);

		return clubPostImage;
	}

	/**
	 * 연관관계 설정 메서드
	 */
	public void setClubPost(ClubPost clubPost) {
		this.clubPost = clubPost;
		clubPost.getImages().add(this);
	}
}
