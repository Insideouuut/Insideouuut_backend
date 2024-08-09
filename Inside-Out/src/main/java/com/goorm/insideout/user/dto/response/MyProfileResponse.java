package com.goorm.insideout.user.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.goorm.insideout.meeting.domain.Category;
import com.goorm.insideout.user.domain.Gender;
import com.goorm.insideout.user.domain.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyProfileResponse {
	private Long userId;
	private String email;
	private String profileImage;
	private String nickname;
	private BigDecimal mannerRating;
	private Gender gender;
	private String phoneNumber;
	private LocalDate birthDate;
	private Set<Category> interests;
	private Set<String> locations;
	private List<ProfileMeetingResponse> pendingMeetings;
	private List<ProfileMeetingResponse> attendedMeetings;
	private List<ProfileMeetingResponse> closedMeetings;
	public MyProfileResponse(User user){
		this.userId=user.getId();
		this.email=user.getEmail();
		this.profileImage=user.getProfileImage().getImage().getUrl();
		this.nickname=user.getNickname();
		this.mannerRating=user.getMannerTemp();
		this.gender=user.getGender();
		this.phoneNumber= user.getPhoneNumber();;
		this.birthDate=user.getBirthDate();
		this.interests=user.getInterests();
		this.locations=user.getLocations();
	}
}
