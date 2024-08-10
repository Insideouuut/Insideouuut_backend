package com.goorm.insideout.meeting.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.insideout.chatroom.domain.ChatRoom;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.meeting.domain.Meeting;
import com.goorm.insideout.meeting.domain.MeetingApply;
import com.goorm.insideout.meeting.domain.MeetingPlace;
import com.goorm.insideout.meeting.domain.MeetingUser;
import com.goorm.insideout.meeting.domain.Progress;
import com.goorm.insideout.meeting.domain.Role;
import com.goorm.insideout.meeting.dto.request.MeetingCreateRequest;
import com.goorm.insideout.meeting.dto.request.MeetingPlaceRequest;
import com.goorm.insideout.meeting.dto.request.SearchRequest;
import com.goorm.insideout.meeting.dto.request.MeetingUpdateRequest;
import com.goorm.insideout.meeting.dto.response.MeetingResponse;
import com.goorm.insideout.meeting.repository.MeetingRepository;
import com.goorm.insideout.meeting.repository.MeetingUserRepository;
import com.goorm.insideout.meeting.repository.PlaceRepository;
import com.goorm.insideout.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetingService {
	private final MeetingRepository meetingRepository;
	private final MeetingUserRepository meetingUserRepository;
	private final PlaceRepository placeRepository;

	@Transactional
	public Meeting save(MeetingCreateRequest request, User user) {
		MeetingPlace meetingPlace = findOrCreatePlace(request.getMeetingPlace());
		Meeting meeting = request.toEntity(user, meetingPlace);

		MeetingUser meetingUser = MeetingUser.of(meeting, user, Role.HOST);
		meetingUserRepository.save(meetingUser);

		return meetingRepository.save(meeting);
	}

	public List<MeetingResponse> findAll() {
		return meetingRepository.findAll()
			.stream()
			.map(MeetingResponse::of)
			.toList();
	}

	public MeetingResponse findById(Long id, User user) {


		Meeting meeting = meetingRepository.findById(id)
			.orElseThrow(() -> ModongException.from(ErrorCode.MEETING_NOT_FOUND));

		return MeetingResponse.of(meeting);
	}

	// 검색 조건에 따른 조회
	public List<MeetingResponse> findByCondition(SearchRequest condition) {
		return meetingRepository.findAllByCondition(condition);
	}

	// 정렬 타입에 따른 조회
	public List<MeetingResponse> findBySortType(SearchRequest condition) {
		return meetingRepository.findBySortType(condition);
	}

	// 검색 조건 및 정렬 타입에 따른 조회
	public List<MeetingResponse> findByConditionAndSortType(SearchRequest condition) {
		return meetingRepository.findByConditionAndSortType(condition);
	}

	public Page<MeetingResponse> findParticipatingMeetings(User user, Pageable pageable) {
		return meetingUserRepository.findOngoingMeetingsByProgress(user.getId(),
				Progress.ONGOING, pageable)
			.map(meetingUser -> MeetingResponse.of(meetingUser.getMeeting()));
	}

	public List<MeetingResponse> findParticipatingMeetings(User user) {
		List<MeetingUser> meetingUsers = meetingUserRepository.findOngoingMeetingsByProgress(user.getId(),
			Progress.ONGOING);
		return meetingUsers.stream()
			.map(meetingUser -> MeetingResponse.of(meetingUser.getMeeting()))
			.collect(Collectors.toList());
	}

	public Page<MeetingResponse> findRunningMeetings(User user, Pageable pageable) {
		return meetingRepository.findRunningMeetings(user.getId(), Progress.ONGOING, pageable)
			.map(MeetingResponse::of);
	}

	public Page<MeetingResponse> findEndedMeetings(User user, Pageable pageable) {
		return meetingUserRepository.findEndedMeetings(user.getId(), Progress.ENDED, pageable)
			.map(meetingUser -> MeetingResponse.of(meetingUser.getMeeting()));
	}

	public List<MeetingResponse> findEndedMeetings(User user) {
		List<MeetingUser> meetingUsers = meetingUserRepository.findEndedMeetings(user.getId(),
			Progress.ENDED);
		return meetingUsers.stream()
			.map(meetingUser -> MeetingResponse.of(meetingUser.getMeeting()))
			.collect(Collectors.toList());
	}

	@Transactional
	public void updateById(User user, Long meetingId, MeetingUpdateRequest request) {
		// 수정할 모임 조회 후, 수정을 요청한 유저가 호스트인지 검증
		Meeting meeting = meetingRepository.findById(meetingId)
			.orElseThrow(() -> ModongException.from(ErrorCode.MEETING_NOT_FOUND));
		validateIsHost(user, meeting);

		MeetingPlace meetingPlace = findOrCreatePlace(request.getMeetingPlace());

		meeting.updateMeeting(request.toEntity(user, meetingPlace));
	}

	@Transactional
	public void deleteById(User user, Long meetingId) {
		// 삭제할 모임 조회 후, 삭제를 요청한 유저가 호스트인지 검증
		Meeting meeting = meetingRepository.findById(meetingId)
			.orElseThrow(() -> ModongException.from(ErrorCode.MEETING_NOT_FOUND));
		validateIsHost(user, meeting);

		meetingRepository.deleteById(meetingId);
	}

	@Transactional
	public void endMeeting(User user, Long meetingId) {
		Meeting meeting = meetingRepository.findById(meetingId)
			.orElseThrow(() -> ModongException.from(ErrorCode.MEETING_NOT_FOUND));
		validateIsHost(user, meeting);

		meeting.changeProgress(Progress.ENDED);
	}

	private void validateIsHost(User user, Meeting meeting) {
		if (!meeting.isHost(user)) {
			throw ModongException.from(ErrorCode.MEETING_NOT_HOST);
		}
	}

	private MeetingPlace findOrCreatePlace(MeetingPlaceRequest request) {
		Optional<MeetingPlace> meetingPlace = placeRepository.findPlaceByNameAndLocation(
			request.getName(),
			request.getLatitude(),
			request.getLongitude()
		);

		return meetingPlace.orElseGet(() -> placeRepository.save(request.toEntity()));
	}

	@Transactional
	public Meeting injectMeetingChatRoom(Long meetingId, ChatRoom newChatRoom) {
		Meeting meeting = meetingRepository.findById(meetingId)
			.orElseThrow(() -> ModongException.from(ErrorCode.MEETING_NOT_FOUND));
		meeting.updateChatRoom(newChatRoom);
		return meetingRepository.save(meeting);

	}
}
