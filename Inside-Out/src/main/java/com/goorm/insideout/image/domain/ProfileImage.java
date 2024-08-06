package com.goorm.insideout.image.domain;

import com.goorm.insideout.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
public class ProfileImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "profile_image_id")
	private Long id;

	@Embedded
	private Image image;

	@OneToOne(fetch = FetchType.LAZY)
	private User user;

	/**
	 * 생성 메서드
	 */
	public static ProfileImage createProfileImage(String uploadName, String storeName, User user) {
		ProfileImage profileImage = new ProfileImage();

		profileImage.image = Image.createImage(uploadName, storeName);
		profileImage.user = user;

		return profileImage;
	}
}