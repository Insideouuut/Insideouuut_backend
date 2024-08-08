package com.goorm.insideout.club.dto.responseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.goorm.insideout.club.dto.ClubDetailOwnerDto;
import com.goorm.insideout.club.dto.ClubDetailUserDto;
import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.club.entity.ClubApply;
import com.goorm.insideout.club.entity.ClubUser;
import com.goorm.insideout.image.domain.ProfileImage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClubApplyResponseDto {

	private Long applyId;
	private String userName;
	private ProfileImage profileImage;
	//private String profileImgUrl;
	private BigDecimal mannerTemp;
	private String answer;


	public static ClubApplyResponseDto of(ClubApply clubApply){
		return new ClubApplyResponseDto(clubApply);
	}

	public ClubApplyResponseDto(ClubApply clubApply) {
		this.applyId = clubApply.getApplyId();
		this.userName = clubApply.getUserName();
		this.profileImage = clubApply.getProfileImage();
		this.mannerTemp = clubApply.getMannerTemp();
		this.answer = clubApply.getAnswer();
	}
}
