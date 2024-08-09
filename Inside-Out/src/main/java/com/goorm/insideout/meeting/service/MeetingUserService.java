package com.goorm.insideout.meeting.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.meeting.domain.Meeting;
import com.goorm.insideout.meeting.domain.MeetingUser;
import com.goorm.insideout.meeting.dto.response.MeetingUserResponse;
import com.goorm.insideout.meeting.repository.MeetingUserRepository;
import com.goorm.insideout.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetingUserService {
	private final MeetingUserRepository meetingUserRepository;

	@Transactional
	public void meetingUserExpel(Long meetingUserId, User host) {
		MeetingUser meetingUser = meetingUserRepository.findById(meetingUserId)
			.orElseThrow(() -> ModongException.from(ErrorCode.MEETING_NOT_MEMBER));
		Meeting meeting = meetingUser.getMeeting();
		validateIsHost(host, meeting);
		meetingUserRepository.deleteById(meetingUserId);
	}

	@Transactional
	public List<MeetingUserResponse> findMeetingUsers(Long meetingId) {
		List<MeetingUser> meetingUsers = meetingUserRepository.findByMeetingId(meetingId);
		return meetingUsers.stream().map(MeetingUserResponse::of).collect(Collectors.toList());
	}

	private void validateIsHost(User user, Meeting meeting) {
		if (!meeting.isHost(user)) {
			throw ModongException.from(ErrorCode.MEETING_NOT_HOST);
		}
	}
}
