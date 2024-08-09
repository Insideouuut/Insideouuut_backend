package com.goorm.insideout.club.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.insideout.club.dto.ClubCommentDto;
import com.goorm.insideout.club.dto.requestDto.ClubCommentRequestDto;
import com.goorm.insideout.club.dto.requestDto.ClubPostRequestDto;
import com.goorm.insideout.club.dto.responseDto.ClubCommentListResponseDto;
import com.goorm.insideout.club.dto.responseDto.ClubPostListResponseDto;
import com.goorm.insideout.club.entity.ClubComment;
import com.goorm.insideout.club.entity.ClubPost;
import com.goorm.insideout.club.entity.ClubUser;
import com.goorm.insideout.club.repository.ClubCommentRepository;
import com.goorm.insideout.club.repository.ClubPostRepository;
import com.goorm.insideout.club.repository.ClubUserRepository;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ClubCommentServiceImpl implements ClubCommentService{

	private final ClubCommentRepository clubCommentRepository;
	private final ClubUserRepository clubUserRepository;
	private final ClubPostRepository clubPostRepository;

	@Override
	public ClubComment saveComment(Long clubId, ClubCommentRequestDto clubCommentRequestDto, Long clubPostId, User user) {

		ClubUser clubUser = clubUserRepository.findByUserIdAndClubId(user.getId(), clubId)
			.orElseThrow(() -> ModongException.from(ErrorCode.USER_NOT_FOUND));


		return clubCommentRepository.save(clubCommentBuilder(clubCommentRequestDto,clubPostId, clubUser));
	}

	@Override
	public List<ClubCommentListResponseDto> findCommentsByClubPostId(Long clubPostId) {


		return clubCommentRepository.findByClubPostIdJQL(clubPostId).stream()
			.map(ClubCommentListResponseDto::new)
			.collect(Collectors.toList());
	}

	@Override
	public void deleteComment(Long clubId, Long clubCommentId, User user) {
		ClubComment clubComment = clubCommentRepository.findById(clubCommentId)
			.orElseThrow(() -> ModongException.from(ErrorCode.CLUB_NOT_FOUND));
		ClubUser clubUser = clubUserRepository.findByUserIdAndClubId(user.getId(), clubId)
			.orElseThrow(() -> ModongException.from(ErrorCode.USER_NOT_FOUND));

		if(!clubComment.getClubUser().getClubUserId().equals(clubUser.getClubUserId())){
			throw new IllegalStateException();
		}

		clubCommentRepository.deleteById(clubCommentId);
	}


	@Override
	public void updateComment(Long clubId, Long clubCommentId, ClubCommentRequestDto clubCommentRequestDto, User user) {
		ClubComment clubComment = clubCommentRepository.findById(clubCommentId)
			.orElseThrow(() -> ModongException.from(ErrorCode.CLUB_NOT_FOUND));
		ClubUser clubUser = clubUserRepository.findByUserIdAndClubId(user.getId(), clubId)
			.orElseThrow(() -> ModongException.from(ErrorCode.USER_NOT_FOUND));

		if(!clubComment.getClubUser().getClubUserId().equals(clubUser.getClubUserId())){
			throw new IllegalStateException();
		}

		clubComment.update(clubCommentRequestDto);

		clubCommentRepository.save(clubComment);
	}

	@Override
	public ClubComment findByCommentId(Long clubCommentId) {
		return clubCommentRepository.findById(clubCommentId).orElseThrow(()->ModongException.from(ErrorCode.CLUB_NOT_FOUND));
	}

	public ClubComment clubCommentBuilder(ClubCommentRequestDto clubCommentRequestDto, Long clubPostId, ClubUser clubUser) {

		return ClubComment.builder()
			.content(clubCommentRequestDto.getContent())
			.dateTime(LocalDateTime.now())
			.writer(clubUser.getUserName())
			.clubUser(clubUser)
			.clubPost(clubPostRepository.findById(clubPostId).orElseThrow(()->ModongException.from(ErrorCode.CLUB_NOT_FOUND)))
			.build();

	}
}
