package com.goorm.insideout.club.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.insideout.club.dto.ClubPostDto;
import com.goorm.insideout.club.dto.requestDto.ClubPostRequestDto;
import com.goorm.insideout.club.dto.responseDto.ClubPostListResponseDto;
import com.goorm.insideout.club.dto.responseDto.ClubPostResponseDto;
import com.goorm.insideout.club.entity.ClubPost;
import com.goorm.insideout.club.entity.ClubUser;
import com.goorm.insideout.club.repository.ClubPostRepository;
import com.goorm.insideout.club.repository.ClubRepository;
import com.goorm.insideout.club.repository.ClubUserRepository;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubPostServiceImpl implements ClubPostService {

	private final ClubPostRepository clubPostRepository;
	private final ClubUserRepository clubUserRepository;

	@Override
	@Transactional
	public ClubPost saveClubPost(Long clubId, ClubPostRequestDto clubPostRequestDto, User user) {
		ClubUser clubUser = clubUserRepository.findByUserIdAndClubId(user.getId(), clubId)
			.orElseThrow(() -> ModongException.from(ErrorCode.USER_NOT_FOUND));

		return clubPostRepository.save(clubPostBuilder(clubPostRequestDto, clubUser));
	}

	@Override
	public ClubPostDto findByClubPostId(Long clubPostId) {
		ClubPost clubPost = clubPostRepository.findById(clubPostId)
			.orElseThrow(() -> ModongException.from(ErrorCode.INVALID_REQUEST));

		return ClubPostDto.of(clubPost);
	}

	@Override
	public List<ClubPostDto> findByClubId(Long clubId) {
		return clubPostRepository.findByClubId(clubId)
			.stream()
			.map(ClubPostDto::of)
			.toList();
	}

	@Override
	public List<ClubPostListResponseDto> findAll() {
		return clubPostRepository.findAll()
			.stream()
			.map(ClubPostListResponseDto::new)
			.toList();
	}

	@Override
	public List<ClubPostListResponseDto> findClubPostByType(Long clubId, String category) {
		return clubPostRepository.findByClubIdAndTypeJQL(clubId, category).stream()
			.map(ClubPostListResponseDto::new)
			.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void deleteClubPost(Long clubId, Long clubPostId, User user) {
		ClubPost clubPost = clubPostRepository.findById(clubPostId)
			.orElseThrow(() -> ModongException.from(ErrorCode.CLUB_NOT_FOUND));

		ClubUser clubUser = clubUserRepository.findByUserIdAndClubId(user.getId(), clubId)
			.orElseThrow(() -> ModongException.from(ErrorCode.USER_NOT_FOUND));

		if (!clubPost.getClubUser().getClubUserId().equals(clubUser.getClubUserId())) {
			throw new IllegalStateException();
		}

		clubPostRepository.deleteById(clubPostId);
	}

	@Override
	@Transactional
	public ClubPost updateClubPost(Long clubId, ClubPostRequestDto clubRequestPostDto, User user, Long clubPostId) {
		ClubPost clubPost = clubPostRepository.findById(clubPostId)
			.orElseThrow(() -> ModongException.from(ErrorCode.INVALID_REQUEST));
		ClubUser clubUser = clubUserRepository.findByUserIdAndClubId(user.getId(), clubId)
			.orElseThrow(() -> ModongException.from(ErrorCode.USER_NOT_FOUND));

		if (!clubPost.getClubUser().getClubUserId().equals(clubUser.getClubUserId())) {
			throw new IllegalStateException();
		}
		clubPost.update(clubRequestPostDto);

		return clubPostRepository.save(clubPost);
	}

	public ClubPost clubPostBuilder(ClubPostRequestDto clubPostRequestDto, ClubUser clubUser) {

		return ClubPost.builder()
			.postTitle(clubPostRequestDto.getPostTitle())
			.postContent(clubPostRequestDto.getPostContent())
			.category(clubPostRequestDto.getCategory())
			.writer(clubUser.getUserName())
			.createTime(LocalDateTime.now())
			.clubUser(clubUser)
			.club(clubUser.getClub())
			.build();

	}
}
