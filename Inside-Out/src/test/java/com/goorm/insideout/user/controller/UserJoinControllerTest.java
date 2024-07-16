package com.goorm.insideout.user.controller;

import static org.awaitility.Awaitility.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.user.dto.request.UserJoinRequestDTO;
import com.goorm.insideout.user.repository.UserRepository;
import com.goorm.insideout.user.service.UserJoinService;

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
	void join_success() throws Exception{
		UserJoinRequestDTO requestDTO = new UserJoinRequestDTO();
		requestDTO.setEmail("test@example.com");
		requestDTO.setName("테스트");
		requestDTO.setPassword("test1234");

		doNothing().when(userJoinService).join(any(UserJoinRequestDTO.class));

		// When
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/join")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(requestDTO)));

		// Then
		resultActions.andExpect(status().isOk())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status.code").value(200))
			.andExpect(jsonPath("$.status.message").value("올바른 요청입니다."));
	}
	@Test
	@DisplayName("회원가입 실패_올바르지 않은 이메일")
	void join_fail_whit_email() throws Exception{
		UserJoinRequestDTO requestDTO = new UserJoinRequestDTO();
		requestDTO.setEmail("testExample.com");
		requestDTO.setName("테스트");
		requestDTO.setPassword("test1234");

		doNothing().when(userJoinService).join(any(UserJoinRequestDTO.class));

		// When
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/join")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(requestDTO)));

		// Then
		resultActions.andExpect(status().isOk())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status.code").value(400))
			.andExpect(jsonPath("$.status.message").value("올바른 email 형식이 아닙니다"));
	}

	@Test
	@DisplayName("회원가입 실패_중복된 유저")
	void join_fail_whit_duplicate() throws Exception{
		UserJoinRequestDTO requestDTO = new UserJoinRequestDTO();
		requestDTO.setEmail("test@example.com");
		requestDTO.setName("테스트");
		requestDTO.setPassword("test1234");

		doThrow(ModongException.from(ErrorCode.DUPLICATE_USER_EMAIL))
			.when(userJoinService).join(any(UserJoinRequestDTO.class));


		// When
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/join")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(requestDTO)));

		// Then
		resultActions.andExpect(status().isOk())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status.code").value(409))
			.andExpect(jsonPath("$.status.message").value("중복된 이메일입니다"));
	}

}
