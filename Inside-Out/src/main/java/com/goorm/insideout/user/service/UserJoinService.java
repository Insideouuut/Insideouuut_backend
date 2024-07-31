package com.goorm.insideout.user.service;

import static com.goorm.insideout.global.exception.ErrorCode.*;

import java.io.IOException;
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
import com.goorm.insideout.user.dto.request.CheckEmailRequest;
import com.goorm.insideout.user.dto.request.CheckNicknameRequest;
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
		List<String> categoryDTO = userJoinRequest.getCategory();
		Set<Category> categorySet = new HashSet<>();
		for (String category : categoryDTO) {
			categorySet.add(Category.valueOf(category));
		}
		validateExistEmail(userJoinRequest.getEmail());
		validateExistNickname(userJoinRequest.getName());
		User joinUser = User.builder()
			.email(userJoinRequest.getEmail())
			.password(bCryptPasswordEncoder.encode(userJoinRequest.getPassword()))
			.name(userJoinRequest.getName())
			.birthDate(userJoinRequest.getBirthDate())
			.phoneNumber(userJoinRequest.getPhoneNumber())
			.interests(categorySet)
			.gender(Gender.valueOf(userJoinRequest.getGender()))
			.location(userJoinRequest.getLocation())
			.nickname(userJoinRequest.getNickName())
			.build();
		userRepository.save(joinUser);
	}

	public void checkFirstLogin(User user, HttpServletResponse response) throws IOException {
		if(user==null){
			throw ModongException.from(USER_NOT_FOUND);
		}
		String nickname= user.getNickname();
		if(nickname==null){
			response.sendRedirect("http://localhost:5173/userinfo");
			//response.sendRedirect("https://modong.link/userinfo");
		}
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
