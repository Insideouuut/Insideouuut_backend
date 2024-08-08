package com.goorm.insideout.global.Initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.user.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer {

	private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

	private final UserRepository userRepository;

	@PostConstruct
	public void initialize() {
		try {
			// Check if SYSTEM_USER already exists
			if (userRepository.findByEmail("system@insideout.com").isEmpty()) {

				User systemUser = User.builder()
					.email("system@insideout.com")
					.password("systempass") // 시스템 사용자이므로, 안전한 비밀번호를 사용하세요.
					.name("SYSTEM")
					.nickname("SYSTEM")
					.build();

				systemUser.initDefaultProfileImage();
				userRepository.save(systemUser);

				logger.info("SYSTEM_USER has been successfully created.");
			} else {
				logger.info("SYSTEM_USER already exists.");
			}
		} catch (Exception e) {
			logger.error("An error occurred while initializing the database.", e);
			// Optionally, rethrow the exception if necessary
			throw ModongException.from(ErrorCode.INTERNAL_SEVER_ERROR);
		}
	}
}