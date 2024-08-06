package com.goorm.insideout.club.dto.responseDto;

import java.time.LocalDateTime;

import com.goorm.insideout.club.entity.ClubApply;
import com.goorm.insideout.club.entity.ClubComment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClubCommentListResponseDto {
	private Long id;
	private String writer;
	private String comment;

	private LocalDateTime dateTime;

	public static ClubCommentListResponseDto of(ClubComment clubComment){
		return new ClubCommentListResponseDto(clubComment);
	}

	public ClubCommentListResponseDto(ClubComment clubComment) {
		this.id = clubComment.getId();
		this.writer = clubComment.getWriter();
		this.comment = clubComment.getContent();
		this.dateTime = clubComment.getDateTime();
	}
}
