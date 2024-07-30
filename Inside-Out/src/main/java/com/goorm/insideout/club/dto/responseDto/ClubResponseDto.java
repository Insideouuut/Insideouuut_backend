package com.goorm.insideout.club.dto.responseDto;

import java.time.LocalDateTime;

import com.goorm.insideout.club.dto.ClubUserDto;
import com.goorm.insideout.club.entity.Club;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClubResponseDto {

	private Long clubId;

	private String message;

	public static ClubResponseDto of(Long clubId, String message){
		ClubResponseDto res = new ClubResponseDto();
		res.setClubId(clubId);
		res.setMessage(message);
		return res;
	}

}