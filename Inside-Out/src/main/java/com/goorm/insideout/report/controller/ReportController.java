package com.goorm.insideout.report.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goorm.insideout.auth.dto.CustomUserDetails;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.global.response.ApiResponse;
import com.goorm.insideout.report.dto.ReportRequest;
import com.goorm.insideout.report.service.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class ReportController {
	private final ReportService reportService;
	@PostMapping("report/{userId}")
	public ApiResponse reportUser(@PathVariable("userId") Long userId,
		@AuthenticationPrincipal CustomUserDetails customUserDetails, @Validated @RequestBody
	ReportRequest reportRequest, Errors errors) {
		validateRequest(errors);
		reportService.reportUser(customUserDetails.getUser(),userId,reportRequest);
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
