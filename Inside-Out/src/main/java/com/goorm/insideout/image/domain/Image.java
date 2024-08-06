package com.goorm.insideout.image.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Image {

	private String uploadName;

	private String storeName;

	/**
	 * 생성 메서드
	 */
	public static Image createImage(String uploadName, String storeName) {
		Image image = new Image();

		image.uploadName = uploadName;
		image.storeName = storeName;

		return image;
	}
}
