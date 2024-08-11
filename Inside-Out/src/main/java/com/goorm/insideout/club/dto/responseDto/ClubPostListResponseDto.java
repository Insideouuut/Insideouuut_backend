package com.goorm.insideout.club.dto.responseDto;

import java.time.LocalDateTime;

import com.goorm.insideout.club.entity.ClubPost;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ClubPostListResponseDto {
	private final String title;
	private final String category;
	private final LocalDateTime createTime;
	private final String writer;
	private final String content;

	public static ClubPostListResponseDto of(ClubPost clubPost){
		return new ClubPostListResponseDto(clubPost);
	}

	public ClubPostListResponseDto(ClubPost clubPost) {
		this.title = clubPost.getPostTitle();
		this.category = clubPost.getCategory();
		this.createTime = clubPost.getCreateTime();
		this.writer = clubPost.getWriter();
		this.content = clubPost.getPostContent();
	}
}
