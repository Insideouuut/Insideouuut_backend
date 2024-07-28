package com.goorm.insideout.club.dto.responseDto;

import com.goorm.insideout.club.dto.ClubDetail;
import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.club.entity.ClubUser;
import com.goorm.insideout.user.domain.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClubDetailResponseDto {

	ClubDetail clubData;

	String role;

	public static ClubDetailResponseDto of(Club club, User user){
		ClubDetailResponseDto res = new ClubDetailResponseDto();

		if(club == null){
			res.setClubData(null);
			res.setRole(null);
		}
		else {
			res.setClubData(ClubDetail.of(club));
			res.setRole("outsider");
			if (club.getOwner().getId().equals(user.getId()))
				res.setRole("owner");
			else {

				for (ClubUser member : club.getMembers()) {
					if (member.getUser().getId().equals(user.getId())) {
						res.setRole("member");
						break;
					}

				}

			}
		}
		return res;
	}
}
