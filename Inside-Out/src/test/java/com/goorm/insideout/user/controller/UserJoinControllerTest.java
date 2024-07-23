package com.goorm.insideout.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goorm.insideout.auth.config.SecurityConfig;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.user.dto.request.UserJoinRequestDTO;
import com.goorm.insideout.user.service.UserJoinService;
import com.goorm.insideout.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringJUnitConfig
@WebMvcTest(UserJoinController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserJoinControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserJoinService userJoinService;

	@MockBean
	private UserRepository userRepository;

	private ObjectMapper objectMapper;

	@BeforeEach
	public void setUp() {
		objectMapper = new ObjectMapper();
	}

	@Test
	@DisplayName("회원가입 성공")
	@WithMockUser
	void join_success() throws Exception {
		// Given
		UserJoinRequestDTO requestDTO = new UserJoinRequestDTO();
		requestDTO.setEmail("test@example.com");
		requestDTO.setName("테스트");
		requestDTO.setPassword("test1234");

		doNothing().when(userJoinService).join(any(UserJoinRequestDTO.class));

		// When
		ResultActions resultActions = mockMvc.perform(post("/api/join")
			.with(csrf()) // CSRF 토큰 추가
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(requestDTO)));

		// Then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("$.status.code").value(200))
			.andExpect(jsonPath("$.status.message").value("올바른 요청입니다."));
	}

	@Test
	@DisplayName("회원가입 실패 - 올바르지 않은 이메일")
	@WithMockUser
	void join_fail_with_invalid_email() throws Exception {
		// Given
		UserJoinRequestDTO requestDTO = new UserJoinRequestDTO();
		requestDTO.setEmail("testExample.com"); // 올바르지 않은 이메일 형식
		requestDTO.setName("테스트");
		requestDTO.setPassword("test1234");

		doNothing().when(userJoinService).join(any(UserJoinRequestDTO.class));

		// When
		ResultActions resultActions = mockMvc.perform(post("/api/join")
			.with(csrf()) // CSRF 토큰 추가
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(requestDTO)));

		// Then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("$.status.code").value(400))
			.andExpect(jsonPath("$.status.message").value("올바른 email 형식이 아닙니다"));
	}

	@Test
	@DisplayName("회원가입 실패 - 중복된 유저")
	@WithMockUser
	void join_fail_with_duplicate_user() throws Exception {
		// Given
		UserJoinRequestDTO requestDTO = new UserJoinRequestDTO();
		requestDTO.setEmail("test@example.com");
		requestDTO.setName("테스트");
		requestDTO.setPassword("test1234");

		doThrow(ModongException.from(ErrorCode.DUPLICATE_USER_EMAIL))
			.when(userJoinService).join(any(UserJoinRequestDTO.class));

		// When
		ResultActions resultActions = mockMvc.perform(post("/api/join")
			.with(csrf()) // CSRF 토큰 추가
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(requestDTO)));

		// Then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("$.status.code").value(409))
			.andExpect(jsonPath("$.status.message").value("중복된 이메일입니다"));
	}
}
