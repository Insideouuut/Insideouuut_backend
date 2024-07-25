package com.goorm.insideout.image.dto.response;

import com.goorm.insideout.image.domain.Image;

import lombok.Getter;

@Getter
public class ImageResponse {
	private String uploadName;
	private String storeName;

	public ImageResponse(Image image) {
		this.uploadName = image.getUploadName();
		this.storeName = image.getStoreName();
	}
}
