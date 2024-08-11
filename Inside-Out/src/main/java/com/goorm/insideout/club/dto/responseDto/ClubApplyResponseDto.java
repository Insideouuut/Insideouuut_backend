package com.goorm.insideout.club.dto.responseDto;

import java.math.BigDecimal;

import com.goorm.insideout.club.entity.ClubApply;
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
	private String profileImgUrl;
	private BigDecimal mannerTemp;


	public static ClubApplyResponseDto of(ClubApply clubApply){
		return new ClubApplyResponseDto(clubApply);
	}

	public ClubApplyResponseDto(ClubApply clubApply) {
		this.applyId = clubApply.getApplyId();
		this.userName = clubApply.getUserName();
		this.profileImgUrl = clubApply.getProfileImgUrl();
		this.mannerTemp = clubApply.getMannerTemp();
	}
}
