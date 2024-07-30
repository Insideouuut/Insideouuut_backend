package com.goorm.insideout.user.controller;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.global.response.ApiResponse;
import com.goorm.insideout.user.dto.request.CheckEmailRequest;
import com.goorm.insideout.user.dto.request.CheckNicknameRequest;
import com.goorm.insideout.user.dto.request.UserJoinRequestDTO;
import com.goorm.insideout.user.service.UserJoinService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserJoinController {
	private final UserJoinService service;

	@PostMapping("/join")
	public ApiResponse join(@Validated @RequestBody UserJoinRequestDTO userJoinRequest, Errors errors) {
		validateRequest(errors);
		service.join(userJoinRequest);
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}
	@GetMapping("/check-email")
	public ApiResponse checkEmail(@Validated @RequestBody CheckEmailRequest checkEmailRequest, Errors errors) {
		validateRequest(errors);
		service.validateExistEmail(checkEmailRequest.getEmail());
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}
	@GetMapping("/check-nickname")
	public ApiResponse checkNickname(@Validated @RequestBody CheckNicknameRequest checkNicknameRequest, Errors errors) {
		validateRequest(errors);
		service.validateExistEmail(checkNicknameRequest.getNickname());
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	private void validateRequest(Errors errors) {
		if (errors.hasErrors()) {
			errors.getFieldErrors().forEach(error -> {
				String errorMessage = error.getDefaultMessage();
				throw ModongException.from(ErrorCode.INVALID_REQUEST,errorMessage);
			});
		}
	}

}
