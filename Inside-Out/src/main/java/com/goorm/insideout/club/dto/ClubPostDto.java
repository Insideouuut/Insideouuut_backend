package com.goorm.insideout.club.dto;

import java.time.LocalDateTime;

import com.goorm.insideout.club.entity.ClubPost;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubPostDto {

	@NotBlank(message = "제목을 입력해주세요.")
	private String title;

	private String category;

	private LocalDateTime dateTime;

	private String writer;

	@NotBlank(message = "내용을 입력하세요.")
	private String content;

	public static ClubPostDto of(ClubPost clubPost){
		return new ClubPostDto(clubPost);
	}

	public ClubPostDto(ClubPost clubPost){
		this.title = clubPost.getPostTitle();
		this.category = clubPost.getCategory();
		this.dateTime = clubPost.getCreateTime();
		this.writer = clubPost.getWriter();
		this.content = clubPost.getPostContent();
	}
}
