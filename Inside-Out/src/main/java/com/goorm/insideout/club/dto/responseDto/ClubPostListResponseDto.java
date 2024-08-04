package com.goorm.insideout.club.dto.responseDto;

import com.goorm.insideout.club.entity.ClubPost;

public class ClubPostListResponseDto {
	private String postTitle;
	private String writer;
	private String content;

	public static ClubPostListResponseDto of(ClubPost clubPost){
		return new ClubPostListResponseDto(clubPost);
	}

	public ClubPostListResponseDto(ClubPost clubPost) {
		this.postTitle = clubPost.getPostTitle();
		this.writer = clubPost.getWriter();
		this.content = clubPost.getPostContent();
	}
}
