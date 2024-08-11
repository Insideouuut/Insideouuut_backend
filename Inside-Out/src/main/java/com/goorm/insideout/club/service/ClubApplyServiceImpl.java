package com.goorm.insideout.club.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.insideout.club.dto.AnswerDto;
import com.goorm.insideout.club.dto.requestDto.ClubApplyRequestDto;
import com.goorm.insideout.club.dto.responseDto.ClubApplyResponseDto;
import com.goorm.insideout.club.dto.responseDto.ClubQuestionAnswerResponseDto;
import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.club.entity.ClubApply;
import com.goorm.insideout.club.entity.ClubQuestionAnswer;
import com.goorm.insideout.club.entity.ClubUser;
import com.goorm.insideout.club.repository.ClubApplyRepository;
import com.goorm.insideout.club.repository.ClubQuestionAnswerRepository;
import com.goorm.insideout.club.repository.ClubRepository;
import com.goorm.insideout.club.repository.ClubUserRepository;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.image.domain.ProfileImage;
import com.goorm.insideout.image.repository.ProfileImageRepository;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class ClubApplyServiceImpl implements ClubApplyService {

	private final ClubApplyRepository clubApplyRepository;
	private final ClubRepository clubRepository;
	private final ProfileImageRepository profileImageRepository;
	private final ClubQuestionAnswerRepository clubQuestionAnswerRepository;

	@Override
	@Transactional
	public ClubApply findClubApplyByUserIDAndClubId(Long userId, Long clubId) {
		return clubApplyRepository.findByUserIdAndClubId(userId, clubId)
			.orElseThrow(() -> ModongException.from(ErrorCode.USER_NOT_FOUND));
	}

	@Modifying(clearAutomatically = true)
	@Transactional
	@Override
	public void clubApplyDelete(Long userId, Long clubId) {
		clubApplyRepository.deleteByUserIdAndClubId(userId, clubId);
	}

	@Override
	@Transactional
	public void clubApply(Club club, User user, ClubApplyRequestDto clubApplyRequestDto, List<AnswerDto> answerDtos) {
		if (clubApplyRepository.findByUserIdAndClubId(user.getId(), club.getClubId()).isPresent()) {
			throw new IllegalStateException();
		}

		String userBrithYear = Arrays.stream(user.getBirthDate().toString().split("-"))
			.findFirst()
			.orElseThrow(() -> ModongException.from(ErrorCode.USER_NOT_FOUND));
		String nowYear = Arrays.stream(LocalDateTime.now().toString().split("-"))
			.findFirst()
			.orElseThrow(() -> ModongException.from(ErrorCode.INVALID_REQUEST));
		int userAge = Integer.valueOf(nowYear) - Integer.valueOf(userBrithYear);

		if (club.getMinAge() > userAge || userAge > club.getMaxAge()) {
			throw new IllegalStateException();
		}

		List<ClubUser> members = getMembers(club.getClubId());

		for (ClubUser clubUser : members) {
			if (user.getId().equals(clubUser.getUserId())) {
				throw new IllegalStateException();
			}
		}
		ProfileImage profileImage = profileImageRepository.findByUserId(user.getId())
			.orElseThrow(() -> ModongException.from(ErrorCode.INVALID_REQUEST));

		ClubApply saveClubApply = clubApplyRepository.save(ClubApply.builder()
			.userId(user.getId())
			.clubId(club.getClubId())
			.userName(user.getName())
			.profileImgUrl(profileImage.getImage().getUrl())
			.mannerTemp(user.getMannerTemp())
			.build());


		// 질문과 답변 저장
		for (AnswerDto answerDto : answerDtos) {
			ClubQuestionAnswer questionAnswer = ClubQuestionAnswer.of(saveClubApply,
				answerDto.getQuestion(), answerDto.getAnswer());
			clubQuestionAnswerRepository.save(questionAnswer);
		}
	}

	@Override
	public List<ClubApplyResponseDto> findApplyList(Club club, User user) {
		List<ClubApply> byClubIdJQL = clubApplyRepository.findByClubIdJQL(club.getClubId());
		List<ClubApplyResponseDto> applyList = new ArrayList<>();

		if (!user.getId().equals(club.getOwner().getId())) {

			throw new IllegalStateException();
		}

		for (ClubApply clubApply : byClubIdJQL) {
			ClubApplyResponseDto clubApplyResponseDto = ClubApplyResponseDto.of(clubApply);
			applyList.add(clubApplyResponseDto);
		}

		return applyList;
	}

	@Override
	public ClubApply findClubApplyById(Long applyId) {
		return clubApplyRepository.findByApplyId(applyId)
			.orElseThrow(() -> ModongException.from(ErrorCode.USER_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public List<ClubUser> getMembers(Long clubId) {

		Club club = this.clubRepository.findById(clubId)
			.orElseThrow(() -> ModongException.from(ErrorCode.CLUB_NOT_FOUND));

		return club.getMembers();
	}

	public List<ClubQuestionAnswerResponseDto> getAnswersByApplyId(Long applyId) {
		List<ClubQuestionAnswer> questionAnswers = clubQuestionAnswerRepository.findByClubApplyId(applyId);

		return questionAnswers.stream()
			.map(answer -> {
				ClubQuestionAnswerResponseDto dto = new ClubQuestionAnswerResponseDto();
				dto.setApplyId(answer.getClubApply().getApplyId());
				dto.setQuestion(answer.getQuestion());
				dto.setAnswer(answer.getAnswer());
				return dto;
			})
			.collect(Collectors.toList());
	}
}
