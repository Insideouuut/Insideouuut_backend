package com.goorm.insideout.club.dto.requestDto;

import java.time.LocalDateTime;

import com.goorm.insideout.club.dto.ClubUserDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ClubDeleteRequestDto {

	private Long clubId;

	private String clubName;

	private String host;
	private String category;

	private LocalDateTime modifiedAt;
	private String content;

	private String region;
	private String question;

	private Integer member_Cunt;
	private Integer price;
	private Integer age_Limit;

	private ClubUserDto clubUserDto;
}
