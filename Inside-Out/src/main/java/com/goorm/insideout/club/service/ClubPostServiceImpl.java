package com.goorm.insideout.club.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.insideout.club.dto.requestDto.ClubPostRequestDto;
import com.goorm.insideout.club.dto.responseDto.ClubPostListResponseDto;
import com.goorm.insideout.club.entity.Club;
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
@Transactional
@RequiredArgsConstructor
public class ClubPostServiceImpl implements ClubPostService{

	private final ClubPostRepository clubPostRepository;
	private final ClubUserRepository clubUserRepository;
	private final ClubRepository clubRepository;

	@Override
	public ClubPost saveClubPost(Long clubId, ClubPostRequestDto clubPostRequestDto, User user) {
		ClubUser clubUser = clubUserRepository.findByUserIdAndClubId(user.getId(), clubId)
			.orElseThrow(() -> ModongException.from(ErrorCode.USER_NOT_FOUND));

		ClubPost clubPost = clubPostBuilder(clubPostRequestDto, clubUser);

		return clubPostRepository.save(clubPost);
	}

	@Override
	public ClubPost findByClubPostId(Long clubPostId) {

		return clubPostRepository.findById(clubPostId)
			.orElseThrow(() -> ModongException.from(ErrorCode.INVALID_REQUEST));
	}

	@Override
	public List<ClubPostListResponseDto> findClubPostByType(Long clubId, String category) {
		return clubPostRepository.findByClubIdAndTypeJQL(clubId, category).stream()
			.map(ClubPostListResponseDto::new)
			.collect(Collectors.toList());
	}

	@Override
	public void deleteClubPost(Long clubId, Long clubPostId, User user) {
		ClubPost clubPost = clubPostRepository.findById(clubPostId)
			.orElseThrow(() -> ModongException.from(ErrorCode.CLUB_NOT_FOUND));

		ClubUser clubUser = clubUserRepository.findByUserIdAndClubId(user.getId(), clubId)
			.orElseThrow(() -> ModongException.from(ErrorCode.USER_NOT_FOUND));

		if(!clubPost.getClubUser().getClubUserId().equals(clubUser.getClubUserId())){
			throw new IllegalStateException();
		}

		clubPostRepository.deleteById(clubPostId);
	}

	@Override
	public ClubPost updateClubPost(Long clubId, ClubPostRequestDto clubRequestPostDto, User user, Long clubPostId) {
		ClubPost clubPost = clubPostRepository.findById(clubPostId).orElseThrow(()->ModongException.from(ErrorCode.INVALID_REQUEST));
		ClubUser clubUser = clubUserRepository.findByUserIdAndClubId(user.getId(), clubId).orElseThrow(()->ModongException.from(ErrorCode.USER_NOT_FOUND));

		if(!clubPost.getClubUser().getClubUserId().equals(clubUser.getClubUserId())){
			throw new IllegalStateException();
		}
		clubPost.update(clubRequestPostDto);

		return clubPostRepository.save(clubPost);
	}

	public ClubPost clubPostBuilder(ClubPostRequestDto clubPostRequestDto, ClubUser clubUser) {

		return ClubPost.builder()
			.postTitle(clubPostRequestDto.getPostTitle())
			.postContent(clubPostRequestDto.getPostContent())
			//.clubImg(clubImgUrl)
			.category(clubPostRequestDto.getCategory())
			.writer(clubUser.getUserName())
			.createTime(LocalDateTime.now())
			.clubUser(clubUser)
			.club(clubUser.getClub())
			.build();

	}
}
