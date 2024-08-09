package com.goorm.insideout.user.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.web.multipart.MultipartFile;

import com.goorm.insideout.auth.dto.CustomUserDetails;
import com.goorm.insideout.image.domain.ProfileImage;
import com.goorm.insideout.image.service.ImageService;
import com.goorm.insideout.meeting.domain.Category;
import com.goorm.insideout.meeting.dto.response.MeetingResponse;
import com.goorm.insideout.meeting.service.MeetingService;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.user.dto.request.ProfileUpdateRequest;
import com.goorm.insideout.user.dto.response.MyProfileResponse;
import com.goorm.insideout.user.dto.response.ProfileMeetingResponse;
import com.goorm.insideout.user.dto.response.ProfileResponse;
import com.goorm.insideout.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserProfileService {
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final ImageService imageService;
	private final MeetingService meetingService;

	public void updateMyProfile(ProfileUpdateRequest profileUpdateRequest, User user) {
		String password = profileUpdateRequest.getPassword();
		String nickname = profileUpdateRequest.getNickname();
		List<String> interests = profileUpdateRequest.getInterests();
		if (password != null) {
			user.setPassword(bCryptPasswordEncoder.encode(password));
		}
		if (nickname != null) {
			user.setNickname(nickname);
		}
		if (!interests.isEmpty()) {
			user.setInterests(stringListToCategorySet(interests));
		}
		userRepository.save(user);
	}

	public void updateMyProfileImage(MultipartFile multipartFile, User user) {
		ProfileImage profileImage = user.getProfileImage();
		if (profileImage != null) {
			imageService.deleteProfileImages(user.getId());
		}
		imageService.saveProfileImage(List.of(multipartFile), user.getId());
	}

	@Transactional
	public ProfileResponse getProfile(User user) {
		User findUser = userRepository.findByIdWithProfileImage(user.getId());
		ProfileResponse response = new ProfileResponse(findUser);

		List<MeetingResponse> pendingMeetings = meetingService.findPendingMeetings(findUser);
		response.setPendingMeetings(meetingResponseToProfile(pendingMeetings));

		List<MeetingResponse> ParticipatingMeetings = meetingService.findParticipatingMeetings(findUser);
		response.setAttendedMeetings(meetingResponseToProfile(ParticipatingMeetings));

		List<MeetingResponse> endedMeetings = meetingService.findEndedMeetings(findUser);
		response.setClosedMeetings(meetingResponseToProfile(endedMeetings));

		return response;
	}

	@Transactional
	public MyProfileResponse getMyProfile(User user) {
		User findUser = userRepository.findByIdWithProfileImage(user.getId());
		MyProfileResponse response = new MyProfileResponse(findUser);

		List<MeetingResponse> pendingMeetings = meetingService.findPendingMeetings(findUser);
		response.setPendingMeetings(meetingResponseToProfile(pendingMeetings));

		List<MeetingResponse> ParticipatingMeetings = meetingService.findParticipatingMeetings(findUser);
		response.setAttendedMeetings(meetingResponseToProfile(ParticipatingMeetings));

		List<MeetingResponse> endedMeetings = meetingService.findEndedMeetings(findUser);
		response.setClosedMeetings(meetingResponseToProfile(endedMeetings));

		return response;
	}

	private List<ProfileMeetingResponse> meetingResponseToProfile(List<MeetingResponse> meetingResponses) {
		List<ProfileMeetingResponse> profileMeetingResponses = new ArrayList<>();
		for (MeetingResponse meetingResponse : meetingResponses) {
			profileMeetingResponses.add(new ProfileMeetingResponse(meetingResponse));
		}
		return profileMeetingResponses;
	}

	private Set<Category> stringListToCategorySet(List<String> categoryDTO) {
		Set<Category> categorySet = new HashSet<>();
		for (String category : categoryDTO) {
			categorySet.add(Category.valueOf(category));
		}
		return categorySet;
	}
}
