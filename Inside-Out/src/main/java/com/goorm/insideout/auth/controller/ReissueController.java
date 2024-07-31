package com.goorm.insideout.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goorm.insideout.auth.service.ReissueService;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReissueController  {
	private final ReissueService reissueService;
	@PostMapping("/reissue")
	ApiResponse reissue(HttpServletRequest request, HttpServletResponse response){
		String newAccess = reissueService.createNewAccessToken(request,response);
		//response 헤더에 토큰 추가
		response.addHeader("Authorization", "Bearer " + newAccess);
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}
}
