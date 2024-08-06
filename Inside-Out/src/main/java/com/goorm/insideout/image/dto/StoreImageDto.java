package com.goorm.insideout.image.dto;

import com.goorm.insideout.image.domain.MeetingImage;
import com.goorm.insideout.image.domain.ProfileImage;
import com.goorm.insideout.meeting.domain.Meeting;
import com.goorm.insideout.user.domain.User;

public record StoreImageDto(String uploadName, String storeName) {

	public ProfileImage toProfileImageEntity(User user, String url) {
		return ProfileImage.createProfileImage(uploadName, storeName, url, user);
	}

	public MeetingImage toMeetingImageEntity(Meeting meeting, String url) {
		return MeetingImage.createMeetingImage(uploadName, storeName, url, meeting);
	}
}
