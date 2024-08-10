package com.goorm.insideout.meeting.dto.response;

import java.math.BigDecimal;

import com.goorm.insideout.image.dto.response.ImageResponse;
import com.goorm.insideout.meeting.domain.MeetingUser;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MeetingUserResponse {
	private Long id;
	private String nickName;
	private ImageResponse profileImage;
	private BigDecimal mannerTemp;

	public static MeetingUserResponse of(MeetingUser meetingUser) {
		MeetingUserResponse response = new MeetingUserResponse();
		response.id = meetingUser.getId();
		response.nickName = meetingUser.getUser().getNickname();
		response.mannerTemp = meetingUser.getUser().getMannerTemp();
		response.profileImage = ImageResponse.from(meetingUser.getUser().getProfileImage().getImage());

		return response;
	}

}
