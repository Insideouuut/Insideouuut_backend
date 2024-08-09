package com.goorm.insideout.image.dto;

import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.club.entity.ClubPost;
import com.goorm.insideout.image.domain.ClubImage;
import com.goorm.insideout.image.domain.ClubPostImage;
import com.goorm.insideout.image.domain.MeetingImage;
import com.goorm.insideout.image.domain.ProfileImage;
import com.goorm.insideout.meeting.domain.Meeting;
import com.goorm.insideout.user.domain.User;

public record StoreImageDto(String uploadName, String storeName) {

	public ProfileImage toProfileImageEntity(User user, String url) {
		return ProfileImage.createProfileImage(uploadName, storeName, url, user);
	}

	public ClubImage toClubImageEntity(Club club, String url) {
		return ClubImage.createClubImage(uploadName, storeName, url, club);
	}

	public ClubPostImage toClubPostImageEntity(ClubPost clubPost, String url) {
		return ClubPostImage.createClubPostImage(uploadName, storeName, url, clubPost);
	}

	public MeetingImage toMeetingImageEntity(Meeting meeting, String url) {
		return MeetingImage.createMeetingImage(uploadName, storeName, url, meeting);
	}
}
