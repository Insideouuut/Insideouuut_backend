package com.goorm.insideout.user.dto.response;

import java.math.BigDecimal;
import java.util.List;

import com.goorm.insideout.user.domain.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponse {
	private Long userId;
	private String profileImage;
	private String nickname;
	private BigDecimal mannerRating;
	private List<ProfileMeetingResponse> pendingMeetings;
	private List<ProfileMeetingResponse> attendedMeetings;
	private List<ProfileMeetingResponse> closedMeetings;
	public ProfileResponse(User user){
		this.userId=user.getId();
		this.profileImage=user.getProfileImage().getImage().getUrl();
		this.nickname=user.getNickname();
		this.mannerRating=user.getMannerTemp();

	}

}
