package com.goorm.insideout.meeting.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.insideout.auth.dto.CustomUserDetails;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.meeting.domain.Meeting;
import com.goorm.insideout.meeting.domain.MeetingUser;
import com.goorm.insideout.meeting.domain.Role;
import com.goorm.insideout.meeting.dto.response.MeetingUserAuthorityResponse;
import com.goorm.insideout.meeting.dto.response.MeetingUserResponse;
import com.goorm.insideout.meeting.repository.MeetingRepository;
import com.goorm.insideout.meeting.repository.MeetingUserRepository;
import com.goorm.insideout.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetingUserService {
	private final MeetingUserRepository meetingUserRepository;
	private final MeetingRepository meetingRepository;

	@Transactional
	public void meetingUserExpel(Long meetingUserId, User host) {
		MeetingUser meetingUser = meetingUserRepository.findById(meetingUserId)
			.orElseThrow(() -> ModongException.from(ErrorCode.MEETING_NOT_MEMBER));
		Meeting meeting = meetingUser.getMeeting();
		validateIsHost(host, meeting);
		meetingUserRepository.deleteById(meetingUserId);
	}

	@Transactional
	public void meetingUserExit(Long meetingId, User user) {
		Meeting meeting = meetingRepository.findById(meetingId)
			.orElseThrow(() -> ModongException.from(ErrorCode.MEETING_NOT_MEMBER));

		MeetingUser meetingUser = meetingUserRepository.findByMeetingIdAndUserId(meeting.getId(), user.getId())
			.orElseThrow(() -> ModongException.from(ErrorCode.MEETING_NOT_MEMBER));

		meetingUserRepository.delete(meetingUser);
	}

	@Transactional
	public List<MeetingUserResponse> findMeetingUsers(Long meetingId) {
		List<MeetingUser> meetingUsers = meetingUserRepository.findByMeetingId(meetingId);
		return meetingUsers.stream().map(MeetingUserResponse::of).collect(Collectors.toList());
	}

	public MeetingUserAuthorityResponse checkUserAuthority(Long meetingId, CustomUserDetails customUserDetails) {
		if (customUserDetails == null) {
			return MeetingUserAuthorityResponse.of(Role.NONE);
		}

		Optional<MeetingUser> optionalMeetingUser = meetingUserRepository.findByMeetingIdAndUserId(meetingId, customUserDetails.getUser().getId());
		if (optionalMeetingUser.isEmpty()) {
			return MeetingUserAuthorityResponse.of(Role.NONE);
		}
		MeetingUser meetingUser = optionalMeetingUser.get();

		return MeetingUserAuthorityResponse.of(meetingUser.getRole());
	}

	private void validateIsHost(User user, Meeting meeting) {
		if (!meeting.isHost(user)) {
			throw ModongException.from(ErrorCode.MEETING_NOT_HOST);
		}
	}
}
