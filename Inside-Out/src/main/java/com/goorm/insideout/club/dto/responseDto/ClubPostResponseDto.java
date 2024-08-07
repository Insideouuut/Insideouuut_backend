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

	private Long ClubPostId;
	private String message;


	public static ClubPostResponseDto of(Long clubPostId, String message){
		ClubPostResponseDto res = new ClubPostResponseDto();
		res.setClubPostId(clubPostId);
		res.setMessage(message);
		return res;
	}
}
