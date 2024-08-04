package com.goorm.insideout.user.controller;

import java.io.IOException;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.goorm.insideout.auth.dto.CustomUserDetails;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.global.response.ApiResponse;
import com.goorm.insideout.user.dto.request.SocialJoinRequest;
import com.goorm.insideout.user.dto.request.UserJoinRequestDTO;
import com.goorm.insideout.user.service.UserJoinService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "UserJoinController", description = "회원가입 관련 API")
public class UserJoinController {
	private final UserJoinService service;

	@PostMapping("/join")
	@Operation(summary = "회원가입 API", description = "회원가입을 하는 API 입니다.")
	public ApiResponse join(@Validated @RequestBody UserJoinRequestDTO userJoinRequest, Errors errors) {
		validateRequest(errors);
		service.join(userJoinRequest);
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	@GetMapping("/check-email")
	@Operation(summary = "이메일 중복체크 API", description = "이메일 중복체크를 하는 API 입니다.")
	public ApiResponse checkEmail(@RequestParam("email") String email) {
		service.validateExistEmail(email);
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	@GetMapping("/check-nickname")
	@Operation(summary = "닉네임 중복체크 API", description = "닉네임 중복체크를 하는 API 입니다.")
	public ApiResponse checkNickname(@RequestParam("nickname") String nickname) {
		service.validateExistNickname(nickname);
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	@GetMapping("/oauth2/userInfo")
	@Operation(summary = "최초 로그인 체크 API", description = "최초 로그인 체크를 하는 API 입니다.")
	public ApiResponse checkFirstLogin(@AuthenticationPrincipal CustomUserDetails userDetails,
		HttpServletResponse response) throws
		IOException {
		service.checkFirstLogin(userDetails.getUser(), response);
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	@PostMapping("/oauth2/userInfo")
	@Operation(summary = "소셜 로그인 API", description = "소셜 로그인을 하는 API 입니다.")
	public ApiResponse insertUserInfo(@RequestBody SocialJoinRequest socialJoinRequest,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		service.socialJoin(userDetails.getUser(), socialJoinRequest);
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	private void validateRequest(Errors errors) {
		if (errors.hasErrors()) {
			errors.getFieldErrors().forEach(error -> {
				String errorMessage = error.getDefaultMessage();
				throw ModongException.from(ErrorCode.INVALID_REQUEST, errorMessage);
			});
		}
	}

}
