package com.goorm.insideout.club.dto.responseDto;

import java.time.LocalDateTime;

import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.club.entity.ClubPost;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClubPostResponseDto {
	private Long Id;

	private Long clubId;

	private String postTitle;

	private String writer;

	private LocalDateTime createTime;
	private String postContent;


	public static ClubPostResponseDto of(ClubPost clubPost){
		ClubPostResponseDto res = new ClubPostResponseDto();

		res.setId(clubPost.getId());
		res.setClubId(clubPost.getClubId());
		res.setPostTitle(clubPost.getPostTitle());
		res.setWriter(clubPost.getWriter());
		res.setCreateTime(clubPost.getCreateTime());
		res.setPostContent(clubPost.getPostContent());

		return res;
	}
}
