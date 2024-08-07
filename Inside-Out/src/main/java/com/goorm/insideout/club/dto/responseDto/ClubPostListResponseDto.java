package com.goorm.insideout.club.dto.responseDto;

import com.goorm.insideout.club.entity.ClubPost;

import lombok.Getter;

@Getter
public class ClubPostListResponseDto {
	private String postTitle;

	private String category;
	private String writer;
	private String content;

	public static ClubPostListResponseDto of(ClubPost clubPost){
		return new ClubPostListResponseDto(clubPost);
	}

	public ClubPostListResponseDto(ClubPost clubPost) {
		this.postTitle = clubPost.getPostTitle();
		this.category = clubPost.getCategory();
		this.writer = clubPost.getWriter();
		this.content = clubPost.getPostContent();
	}
}
