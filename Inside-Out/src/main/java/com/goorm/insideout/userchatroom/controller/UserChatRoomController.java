package com.goorm.insideout.userchatroom.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goorm.insideout.auth.dto.CustomUserDetails;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.response.ApiResponse;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.userchatroom.service.UserChatRoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserChatRoomController {

	private final UserChatRoomService userChatRoomService;

	// 채팅방 입장
	@PostMapping("/chatroom/{chatRoomId}/enter")
	public ApiResponse enterChatRoom(@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@PathVariable Long chatRoomId) {
		User user = customUserDetails.getUser();
		try {
			userChatRoomService.updateChatRoomConfigTime(chatRoomId, user);
			return new ApiResponse<>(ErrorCode.REQUEST_OK);
		} catch (Exception e) {
			return new ApiResponse<>(ErrorCode.INVALID_REQUEST);
		}
	}

	// 채팅방 퇴장
	@PostMapping("/chatroom/{chatRoomId}/exit")
	public ApiResponse exitChatRoom(@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@PathVariable Long chatRoomId) {
		User user = customUserDetails.getUser();
		try {
			userChatRoomService.updateChatRoomConfigTime(chatRoomId, user);
			return new ApiResponse<>(ErrorCode.REQUEST_OK);
		} catch (Exception e) {
			return new ApiResponse<>(ErrorCode.INVALID_REQUEST);
		}
	}
}
