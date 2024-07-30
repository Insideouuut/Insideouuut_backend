package com.goorm.insideout.meeting.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.meeting.domain.ApprovalStatus;
import com.goorm.insideout.meeting.domain.Meeting;
import com.goorm.insideout.meeting.domain.MeetingPlace;
import com.goorm.insideout.meeting.domain.Progress;
import com.goorm.insideout.meeting.dto.request.MeetingCreateRequest;
import com.goorm.insideout.meeting.dto.request.MeetingSearchRequest;
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
	public Long save(MeetingCreateRequest request, User user) {
		MeetingPlace meetingPlace = findOrCreatePlace(request.getMeetingPlace());

		Meeting meeting = request.toEntity(user, meetingPlace);
		meetingRepository.save(meeting);

		return meeting.getId();
	}

	public MeetingResponse findById(Long id) {
		Meeting meeting = meetingRepository.findById(id)
			.orElseThrow(() -> ModongException.from(ErrorCode.MEETING_NOT_FOUND));

		return new MeetingResponse(meeting);
	}

	// 검색
	public Page<MeetingResponse> findByCondition(MeetingSearchRequest condition, Pageable pageable) {
		return meetingRepository.findAllByCondition(condition, pageable);
	}

	// 검색 및 정렬
	public Page<MeetingResponse> findBySortType(MeetingSearchRequest condition, Pageable pageable) {
		return meetingRepository.findAllBySortType(condition, pageable);
	}

	public Page<MeetingResponse> findPendingMeetings(Long userId, Pageable pageable) {
		return meetingUserRepository.findOngoingMeetingsByProgress(userId, ApprovalStatus.PENDING, Progress.ONGOING, pageable)
			.map(meetingUser -> MeetingResponse.from(meetingUser.getMeeting()));
	}

	public Page<MeetingResponse> findParticipatingMeetings(Long userId, Pageable pageable) {
		return meetingUserRepository.findOngoingMeetingsByProgress(userId, ApprovalStatus.APPROVED, Progress.ONGOING, pageable)
			.map(meetingUser -> MeetingResponse.from(meetingUser.getMeeting()));
	}

	public Page<MeetingResponse> findRunningMeetings(Long hostId, Pageable pageable) {
		return meetingRepository.findRunningMeetings(hostId, Progress.ONGOING, pageable)
			.map(MeetingResponse::from);
	}

	public Page<MeetingResponse> findEndedMeetings(Long userId, Pageable pageable) {
		return meetingUserRepository.findEndedMeetings(userId, ApprovalStatus.APPROVED, Progress.ENDED, pageable)
			.map(meetingUser -> MeetingResponse.from(meetingUser.getMeeting()));
	}

	@Transactional
	public void updateById(User user, Long meetingId, MeetingUpdateRequest request) {
		// 수정할 모임 조회 후, 수정을 요청한 유저가 호스트인지 검증
		Meeting meeting = meetingRepository.findById(meetingId)
			.orElseThrow(() -> ModongException.from(ErrorCode.MEETING_NOT_FOUND));
		validateIsHost(user, meeting);

		meeting.updateMeeting(request.toEntity(user));
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

	private MeetingPlace findOrCreatePlace(MeetingCreateRequest.MeetingPlaceRequest request) {
		Optional<MeetingPlace> meetingPlace = placeRepository.findPlaceByNameAndLocation(
			request.getName(),
			request.getLatitude(),
			request.getLongitude()
		);

		return meetingPlace.orElseGet(() -> placeRepository.save(request.toEntity()));
	}
}