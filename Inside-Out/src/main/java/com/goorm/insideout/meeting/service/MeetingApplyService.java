package com.goorm.insideout.meeting.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.meeting.domain.Meeting;
import com.goorm.insideout.meeting.domain.MeetingApply;
import com.goorm.insideout.meeting.dto.response.MeetingApplyResponse;
import com.goorm.insideout.meeting.repository.MeetingApplyRepository;
import com.goorm.insideout.meeting.repository.MeetingRepository;
import com.goorm.insideout.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingApplyService {
	private final MeetingApplyRepository meetingApplyRepository;
	private final MeetingRepository meetingRepository;

	// 나이 계산
	public static int calculateAge(LocalDate birthDate) {
		LocalDate today = LocalDate.now();
		if (birthDate != null) {
			return Period.between(birthDate, today).getYears();
		}
		throw ModongException.from(ErrorCode.AGE_NOT_FOUND);
	}

	// 모임 참여 신청
	@Transactional
	public void meetingApply(User user, Long meetingId) {
		Meeting meeting = meetingRepository.findById(meetingId)
			.orElseThrow(() -> ModongException.from(ErrorCode.MEETING_NOT_FOUND));

		int minimumAge = meeting.getMinimumAge();
		int maximumAge = meeting.getMaximumAge();
		int currentParticipantsNumber = meeting.getParticipantsNumber();
		int participantLimit = meeting.getParticipantLimit();

		// 유저 나이 검증
		LocalDate birthDate = user.getBirthDate();
		int userAge = calculateAge(birthDate);
		if (userAge < minimumAge || userAge > maximumAge) {
			throw ModongException.from(ErrorCode.USER_AGE_NOT_IN_RANGE);
		}

		// 최대 수용 인원 검증
		if (currentParticipantsNumber >= participantLimit) {
			throw ModongException.from(ErrorCode.MEETING_PARTICIPANT_LIMIT_EXCEEDED);
		}

		// 모임 멤버 신청
		MeetingApply meetingApplyUser = MeetingApply.of(user, meeting);
		meetingApplyRepository.save(meetingApplyUser);

	}

	// 모임 신청 수락
	@Transactional
	public Meeting meetingUserAccept(User host, Long applyId) {
		MeetingApply meetingApply = meetingApplyRepository.findById(applyId)
			.orElseThrow(() -> ModongException.from(ErrorCode.APPLY_NOT_FOUND));

		Meeting meeting = meetingApply.getMeeting();
		int memberCount = meeting.getParticipantsNumber();
		int memberLimit = meeting.getParticipantLimit();

		validateIsHost(host, meeting);

		if (memberCount >= memberLimit) {
			throw ModongException.from(ErrorCode.MEETING_PARTICIPANT_LIMIT_EXCEEDED);
		}

		meeting.increaseParticipantsNumber();
		meeting.updateMeeting(meeting);

		meetingApplyRepository.delete(meetingApply);
		return meeting;
	}
	// 모임 신청 거부
	@Transactional
	public void meetingUserReject(User host, Long applyId) {
		MeetingApply meetingApply = meetingApplyRepository.findById(applyId)
			.orElseThrow(() -> ModongException.from(ErrorCode.APPLY_NOT_FOUND));

		Meeting meeting = meetingApply.getMeeting();

		validateIsHost(host, meeting);

		meetingApplyRepository.deleteById(applyId);
	}

	// 모임 신청 조회
	public List<MeetingApplyResponse> findApplyList(Long meetingId, User host) {
		Meeting meeting = meetingRepository.findById(meetingId)
			.orElseThrow(() -> ModongException.from(ErrorCode.MEETING_NOT_FOUND));

		validateIsHost(host, meeting);

		List<MeetingApply> applies = meetingApplyRepository.findByMeetingId(meetingId);

		return applies.stream()
			.map(MeetingApplyResponse::of)
			.collect(Collectors.toList());
	}

	private void validateIsHost(User user, Meeting meeting) {
		if (!meeting.isHost(user)) {
			throw ModongException.from(ErrorCode.MEETING_NOT_HOST);
		}
	}

}
