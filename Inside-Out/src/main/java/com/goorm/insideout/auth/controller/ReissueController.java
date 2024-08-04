package com.goorm.insideout.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goorm.insideout.auth.service.ReissueService;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "ReissueController", description = "Access Token 재발급 관련 API")
public class ReissueController  {
	private final ReissueService reissueService;
	@PostMapping("/reissue")
	@Operation(summary = "Access Token 재발급 API", description = "Access Token을 재발급하는 API 입니다.")
	ApiResponse reissue(HttpServletRequest request, HttpServletResponse response){
		String newAccess = reissueService.createNewAccessToken(request,response);
		//response 헤더에 토큰 추가
		response.addHeader("Authorization", "Bearer " + newAccess);
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}
}
