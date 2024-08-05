package com.goorm.insideout.user.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goorm.insideout.auth.dto.CustomUserDetails;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.response.ApiResponse;
import com.goorm.insideout.user.dto.request.LocationVerifiedRequest;
import com.goorm.insideout.user.dto.request.SocialJoinRequest;
import com.goorm.insideout.user.service.UserLocationService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserLocationController {
	private final UserLocationService userLocationService;
	@PostMapping("/users/location")
	@Operation(summary = "동네인증 API", description = "동네인증을 하는 API 입니다.")
	public ApiResponse insertUserLocation(@RequestBody LocationVerifiedRequest locationVerifiedRequest,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		userLocationService.insertUserLocation(locationVerifiedRequest,userDetails.getUser());
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}
}
