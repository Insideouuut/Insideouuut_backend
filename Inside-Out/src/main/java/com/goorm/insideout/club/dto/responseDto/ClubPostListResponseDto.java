package com.goorm.insideout.club.dto.responseDto;

import java.time.LocalDateTime;
import java.util.List;

import com.goorm.insideout.club.entity.ClubPost;
import com.goorm.insideout.image.dto.response.ImageResponse;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ClubPostListResponseDto {
	private final Long postId;
	private final String title;
	private final String category;
	private final LocalDateTime createTime;
	private final String writer;
	private final String content;
	private List<ImageResponse> images;

	public static ClubPostListResponseDto of(ClubPost clubPost){
		return new ClubPostListResponseDto(clubPost);
	}

	public ClubPostListResponseDto(ClubPost clubPost) {
		this.postId = clubPost.getId();
		this.title = clubPost.getPostTitle();
		this.category = clubPost.getCategory();
		this.createTime = clubPost.getCreateTime();
		this.writer = clubPost.getWriter();
		this.content = clubPost.getPostContent();
		this.images = clubPost.getImages()
			.stream()
			.map(clubPostImage -> ImageResponse.from(clubPostImage.getImage()))
			.toList();
	}
}
