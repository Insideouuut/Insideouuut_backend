package com.goorm.insideout.meeting.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.insideout.club.repository.ClubUserRepository;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.meeting.domain.Meeting;
import com.goorm.insideout.meeting.domain.MeetingApply;
import com.goorm.insideout.meeting.domain.MeetingQuestionAnswer;
import com.goorm.insideout.meeting.domain.MeetingUser;
import com.goorm.insideout.meeting.domain.Progress;
import com.goorm.insideout.meeting.domain.Role;
import com.goorm.insideout.meeting.dto.AnswerDto;
import com.goorm.insideout.meeting.dto.response.MeetingApplyResponse;
import com.goorm.insideout.meeting.dto.response.MeetingQuestionAnswerResponse;
import com.goorm.insideout.meeting.dto.response.MeetingResponse;
import com.goorm.insideout.meeting.repository.MeetingApplyRepository;
import com.goorm.insideout.meeting.repository.MeetingQuestionAnswerRepository;
import com.goorm.insideout.meeting.repository.MeetingRepository;
import com.goorm.insideout.meeting.repository.MeetingUserRepository;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.userchatroom.service.UserChatRoomService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingApplyService {
	private final ClubUserRepository clubUserRepository;
	private final MeetingApplyRepository meetingApplyRepository;
	private final MeetingRepository meetingRepository;
	private final MeetingUserRepository meetingUserRepository;
	private final MeetingQuestionAnswerRepository meetingQuestionAnswerRepository;
	private final UserChatRoomService userChatRoomService;

	// 모임 참여 신청
	@Transactional
	public void meetingApply(User user, Long meetingId, List<AnswerDto> answers) {
		Meeting meeting = meetingRepository.findById(meetingId)
			.orElseThrow(() -> ModongException.from(ErrorCode.MEETING_NOT_FOUND));

		// 이미 가입되어있으면 에러 처리
		Optional<MeetingUser> existingMeetingUser = meetingUserRepository.findByMeetingIdAndUserId(meetingId,
			user.getId());
		if (existingMeetingUser.isPresent()) {
			throw ModongException.from(ErrorCode.MEETING_ALREADY_JOINED);
		}

		// 이미 가입신청되있는지 확인
		Optional<MeetingApply> existingMeetingApply = meetingApplyRepository.findByMeetingIdAndUserId(meetingId,
			user.getId());
		if (existingMeetingApply.isPresent()) {
			throw ModongException.from(ErrorCode.MEETING_ALREADY_APPLY);
		}

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
		MeetingApply saveMeetingApply = meetingApplyRepository.save(meetingApplyUser);

		// 질문과 답변 저장
		for (AnswerDto answerDto : answers) {
			MeetingQuestionAnswer questionAnswer = MeetingQuestionAnswer.of(saveMeetingApply,
				answerDto.getQuestion(), answerDto.getAnswer());
			meetingQuestionAnswerRepository.save(questionAnswer);
		}
	}

	@Transactional
	public Long clubMeetingApply(User user, Long meetingId) {
		Meeting meeting = meetingRepository.findById(meetingId)
			.orElseThrow(() -> ModongException.from(ErrorCode.MEETING_NOT_FOUND));

		// 동아리 모임인지 확인
		if (meeting.getClub() == null) {
			throw ModongException.from(ErrorCode.MEETING_NOT_BELONG_CLUB);
		}

		// 사용자가 해당 동아리의 멤버인지 확인
		Long clubId = meeting.getClub().getClubId();
		if (!clubUserRepository.clubUserExistByClubId(clubId)) {
			throw ModongException.from(ErrorCode.CLUB_USER_NOT_FOUND);
		}

		// 이미 가입되어있으면 에러 처리
		Optional<MeetingUser> existingMeetingUser = meetingUserRepository.findByMeetingIdAndUserId(meetingId,
			user.getId());
		if (existingMeetingUser.isPresent()) {
			throw ModongException.from(ErrorCode.MEETING_ALREADY_JOINED);
		}

		// 유저 나이 검증
		int minimumAge = meeting.getMinimumAge();
		int maximumAge = meeting.getMaximumAge();
		LocalDate birthDate = user.getBirthDate();
		int userAge = calculateAge(birthDate);
		if (userAge < minimumAge || userAge > maximumAge) {
			throw ModongException.from(ErrorCode.USER_AGE_NOT_IN_RANGE);
		}

		// 최대 수용 인원 검증
		int currentParticipantsNumber = meeting.getParticipantsNumber();
		int participantLimit = meeting.getParticipantLimit();
		if (currentParticipantsNumber >= participantLimit) {
			throw ModongException.from(ErrorCode.MEETING_PARTICIPANT_LIMIT_EXCEEDED);
		}

		// 모임 참여자 수 수정
		meeting.increaseParticipantsNumber();
		Meeting savedMeeting = meetingRepository.save(meeting);

		// 모임 유저에 등록
		MeetingUser meetingUser = MeetingUser.of(savedMeeting, user, Role.MEMBER);

		// 채팅방에 초대
		userChatRoomService.inviteUserToChatRoom(meeting.getChatRoom().getId(), meetingUser.getUser());

		return meetingUserRepository.save(meetingUser).getId();
	}

	// 모임 신청 수락
	@Transactional
	public Long meetingUserAccept(User host, Long applyId) {
		MeetingApply meetingApply = meetingApplyRepository.findById(applyId)
			.orElseThrow(() -> ModongException.from(ErrorCode.APPLY_NOT_FOUND));

		Meeting meeting = meetingApply.getMeeting();
		int memberCount = meeting.getParticipantsNumber();
		int memberLimit = meeting.getParticipantLimit();

		validateIsHost(host, meeting);

		if (memberCount >= memberLimit) {
			throw ModongException.from(ErrorCode.MEETING_PARTICIPANT_LIMIT_EXCEEDED);
		}

		// 모임 참여자 수 수정
		meeting.increaseParticipantsNumber();
		Meeting savedMeeting = meetingRepository.save(meeting);

		// 모임 유저에 등록
		MeetingUser meetingUser = MeetingUser.of(savedMeeting, meetingApply.getUser(), Role.MEMBER);
		meetingApplyRepository.delete(meetingApply);

		// 채팅방에 초대
		userChatRoomService.inviteUserToChatRoom(meeting.getChatRoom().getId(), meetingUser.getUser());

		return meetingUserRepository.save(meetingUser).getId();
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

	// 지원 신청 id를 통해 응답 가져오기
	public List<MeetingQuestionAnswerResponse> getAnswersByApplyId(Long applyId) {
		List<MeetingQuestionAnswer> questionAnswers = meetingQuestionAnswerRepository.findByMeetingApplyId(applyId);

		return questionAnswers.stream()
			.map(answer -> {
				MeetingQuestionAnswerResponse dto = new MeetingQuestionAnswerResponse();
				dto.setApplyId(answer.getMeetingApply().getId());
				dto.setQuestion(answer.getQuestion());
				dto.setAnswer(answer.getAnswer());
				return dto;
			})
			.collect(Collectors.toList());
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

	// 모임 신청 대기 중인 사람 조회
	public Page<MeetingResponse> findPendingMeetings(User user, Pageable pageable) {
		return meetingApplyRepository.findOngoingMeetingsByProgress(user.getId(), Progress.ONGOING, pageable)
			.map(meetingUser -> {
				Meeting meeting = meetingUser.getMeeting();
				return MeetingResponse.of(meeting);
			});
	}

	// 모임 신청 대기 중인 사람 조회
	public List<MeetingResponse> findPendingMeetings(User user) {
		List<MeetingApply> meetingUsers = meetingApplyRepository.findOngoingMeetingsByProgress(user.getId(),
			Progress.ONGOING);
		return meetingUsers.stream()
			.map(meetingUser -> MeetingResponse.of(meetingUser.getMeeting()))
			.collect(Collectors.toList());
	}

	// 호스트인지 검증
	private void validateIsHost(User user, Meeting meeting) {
		if (!meeting.isHost(user)) {
			throw ModongException.from(ErrorCode.MEETING_NOT_HOST);
		}
	}

	// 나이 계산
	private int calculateAge(LocalDate birthDate) {
		LocalDate today = LocalDate.now();
		if (birthDate != null) {
			return Period.between(birthDate, today).getYears();
		}
		throw ModongException.from(ErrorCode.AGE_NOT_FOUND);
	}

}
