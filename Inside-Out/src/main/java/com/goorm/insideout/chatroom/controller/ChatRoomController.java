package com.goorm.insideout.chatroom.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goorm.insideout.auth.dto.CustomUserDetails;
import com.goorm.insideout.chatroom.dto.response.ChatRoomResponseDTO;
import com.goorm.insideout.chatroom.service.ChatRoomService;
import com.goorm.insideout.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatRoomController {

	private final ChatRoomService chatRoomService;

	// 한 유저가 속한 전체 채팅방 가져오기
	@GetMapping("/chatrooms")
	public ApiResponse<List<ChatRoomResponseDTO>> getChatRoomsByUserId(
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		Long userId = customUserDetails.getUser().getId();
		// 사용자 정보를 기반으로 로직 수행
		List<ChatRoomResponseDTO> chatRooms = chatRoomService.getChatRoomsByUserId(userId);
		return new ApiResponse<List<ChatRoomResponseDTO>>(chatRooms);
	}

	// 한 유저가 속한 동아리 채팅방 가져오기
	@GetMapping("/chatrooms/club")
	public ApiResponse<List<ChatRoomResponseDTO>> getClubRoomsByUserId(
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		Long userId = customUserDetails.getUser().getId();
		List<ChatRoomResponseDTO> chatRooms = chatRoomService.getClubRoomsByUserId(userId);
		return new ApiResponse<List<ChatRoomResponseDTO>>(chatRooms);
	}

	// 한 유저가 속한 모임 채팅방 가져오기
	@GetMapping("/chatrooms/meeting")
	public ApiResponse<List<ChatRoomResponseDTO>> getMeetingRoomsByUserId(
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		Long userId = customUserDetails.getUser().getId();
		List<ChatRoomResponseDTO> chatRooms = chatRoomService.getMeetingRoomsByUserId(userId);
		return new ApiResponse<List<ChatRoomResponseDTO>>(chatRooms);
	}

}
