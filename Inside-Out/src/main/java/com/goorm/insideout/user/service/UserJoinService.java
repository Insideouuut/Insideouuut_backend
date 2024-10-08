package com.goorm.insideout.user.service;

import static com.goorm.insideout.global.exception.ErrorCode.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.meeting.domain.Category;
import com.goorm.insideout.user.domain.Gender;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.user.dto.request.SocialJoinRequest;
import com.goorm.insideout.user.dto.request.UserJoinRequestDTO;
import com.goorm.insideout.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserJoinService {
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Transactional
	public void join(UserJoinRequestDTO userJoinRequest) {
		validateExistEmail(userJoinRequest.getEmail());
		validateExistNickname(userJoinRequest.getNickName());
		User joinUser = User.builder()
			.email(userJoinRequest.getEmail())
			.password(bCryptPasswordEncoder.encode(userJoinRequest.getPassword()))
			.name(userJoinRequest.getName())
			.birthDate(userJoinRequest.getBirthDate())
			.phoneNumber(userJoinRequest.getPhoneNumber())
			.mannerTemp(BigDecimal.valueOf(36.5))
			.interests(stringListToCategorySet(userJoinRequest.getCategory()))
			.isLocationVerified(false)
			.gender(Gender.valueOf(userJoinRequest.getGender()))
			.nickname(userJoinRequest.getNickName())
			.build();
		joinUser.initDefaultProfileImage();

		userRepository.save(joinUser);
	}

	public void checkFirstLogin(User user, HttpServletResponse response) throws IOException {
		if (user == null) {
			throw ModongException.from(USER_NOT_FOUND);
		}
		String nickname = user.getNickname();
		if (nickname == null) {
			throw ModongException.from(USER_NOT_FOUND,"사용자 정보 입력이 필요합니다");
		}
	}

	public void socialJoin(User user, SocialJoinRequest socialJoinRequest) {
		if (user == null) {
			throw ModongException.from(USER_NOT_FOUND);
		}
		user.setBirthDate(socialJoinRequest.getBirthDate());
		user.setPhoneNumber(socialJoinRequest.getPhoneNumber());
		user.setMannerTemp(BigDecimal.valueOf(36.5));
		user.setInterests(stringListToCategorySet(socialJoinRequest.getCategory()));
		user.setLocationVerified(false);
		user.setGender(Gender.valueOf(socialJoinRequest.getGender()));
		user.setNickname(socialJoinRequest.getNickName());
		userRepository.save(user);
	}

	private Set<Category> stringListToCategorySet(List<String> categoryDTO) {
		Set<Category> categorySet = new HashSet<>();
		for (String category : categoryDTO) {
			categorySet.add(Category.valueOf(category));
		}
		return categorySet;
	}

	public void validateExistEmail(String email) {
		if (userRepository.existsByEmail(email)) {
			throw ModongException.from(ErrorCode.DUPLICATE_USER_EMAIL);
		}
	}

	public void validateExistNickname(String nickname) {
		if (userRepository.existsByEmail(nickname)) {
			throw ModongException.from(ErrorCode.DUPLICATE_USER_NICKNAME);
		}
	}
}
