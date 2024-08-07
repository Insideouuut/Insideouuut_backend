package com.goorm.insideout.image.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Image {

	private String uploadName;

	private String storeName;

	private String url;

	/**
	 * 생성 메서드
	 */
	public static Image of(String uploadName, String storeName, String url) {
		Image image = new Image();

		image.uploadName = uploadName;
		image.storeName = storeName;
		image.url = url;

		return image;
	}
}
