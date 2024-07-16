package com.goorm.insideout.user.service;

import org.springframework.stereotype.Service;

import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.user.dto.request.UserJoinRequestDTO;
import com.goorm.insideout.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserJoinService {
	private final UserRepository userRepository;

	public void join(UserJoinRequestDTO userJoinRequest) {

		validateExistUser(userJoinRequest);
		User joinUser = User.builder()
			.email(userJoinRequest.getEmail())
			.password(userJoinRequest.getPassword())
			.name(userJoinRequest.getName())
			.build();
		userRepository.save(joinUser);
	}


	private void validateExistUser(UserJoinRequestDTO userJoinRequest) {
		String email = userJoinRequest.getEmail();
		if (userRepository.existsByEmail(email)) {
			throw ModongException.from(ErrorCode.DUPLICATE_USER_EMAIL);
		}
	}
}
