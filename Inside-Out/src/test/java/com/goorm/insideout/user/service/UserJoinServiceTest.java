package com.goorm.insideout.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.user.dto.request.UserJoinRequestDTO;
import com.goorm.insideout.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserJoinServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserJoinService userJoinService;
	@Test
	@DisplayName("중복된 이메일 검증")
	void join_with_duplicate_email_throws_ModongException() {
		// Given
		UserJoinRequestDTO requestDTO = new UserJoinRequestDTO();
		requestDTO.setEmail("test@example.com");
		requestDTO.setName("테스트");
		requestDTO.setPassword("test1234");

		// Mocking the userRepository behavior to return true, indicating duplicate email
		when(userRepository.existsByEmail(requestDTO.getEmail())).thenReturn(true);

		// When, Then
		assertThrows(ModongException.class, () -> {
			userJoinService.join(requestDTO);
		}, "예외처리 발생");

		// Verify that userRepository.existsByEmail() was called exactly once with the correct email
		verify(userRepository, times(1)).existsByEmail(requestDTO.getEmail());

		// Verify that userRepository.save() was never called
		verify(userRepository, never()).save(any(User.class));
	}


}
