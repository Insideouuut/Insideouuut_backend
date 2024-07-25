package com.goorm.insideout.club.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.goorm.insideout.club.dto.responseDto.ClubModifyResponseDto;
import com.goorm.insideout.club.entity.Club;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClubDetail extends ClubModifyResponseDto {
	ClubDetailOwnerDto owner;
	List<ClubDetailUserDto> members = new ArrayList<>();
	private LocalDateTime createDateTime;

	public static ClubDetail of(Club club){
		return new ClubDetail(club);
	}

	public ClubDetail(Club club){
		super(club);
		this.members = ClubDetailUserDto.of(club.getMembers());
		this.owner = ClubDetailOwnerDto.of(club.getOwner());
		this.createdAt = club.getCreatedAt();
	}
}
