package com.goorm.insideout.club.dto.responseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClubCommentResponseDto {
	private Long clubCommentId;
	private String message;


	public static ClubCommentResponseDto of(Long clubCommentId, String message){
		ClubCommentResponseDto res = new ClubCommentResponseDto();
		res.setClubCommentId(clubCommentId);
		res.setMessage(message);
		return res;
	}
}
