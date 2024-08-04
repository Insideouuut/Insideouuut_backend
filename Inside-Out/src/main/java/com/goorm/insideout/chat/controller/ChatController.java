package com.goorm.insideout.chat.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.goorm.insideout.auth.dto.CustomUserDetails;
import com.goorm.insideout.chat.domain.Chat;
import com.goorm.insideout.chat.dto.request.ChatRequestDTO;
import com.goorm.insideout.chat.dto.response.ChatResponseDTO;
import com.goorm.insideout.chat.dto.response.InitialChatListResponseDTO;
import com.goorm.insideout.chat.service.ChatService;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "CharController", description = "채팅 관련 API")
public class ChatController {
	private final ChatService chatService;
	private final SimpMessageSendingOperations messagingTemplate;

	// 메세지 전송
	@MessageMapping("/chatRoom/{chatRoomId}")
	@Operation(summary = "채팅방 메세지 전송 API", description = "채팅방에 메세지를 전송하는 API 입니다.")
	public ApiResponse messageHandler(@DestinationVariable("chatRoomId") Long roomId,
		@RequestBody ChatRequestDTO chatRequestDTO, Principal principal) {
		String email = principal.getName();
		Chat chat = chatService.createChat(roomId, chatRequestDTO, email);
		ChatResponseDTO chatResponseDTO = ChatResponseDTO.builder()
			.content(chat.getContent())
			.sender(chat.getUser().getName())
			.sendTime(chat.getSendTime())
			.build();
		messagingTemplate.convertAndSend("/sub/chatRoom/" + roomId, chatResponseDTO);
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	// 채팅방 입장시 메세지 가져오기 (읽은 메세지 + 안읽은 메세지) 자연스러운 흐름을 위해
	@GetMapping("/api/chat/initial/chatroom/{chatRoomId}")
	@Operation(summary = "채팅방 초기 메세지 조회 API", description = "채팅방에 입장시 초기 메세지를 조회하는 API 입니다.")
	public ApiResponse<InitialChatListResponseDTO> getInitialMessages(@PathVariable Long chatRoomId,
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		Long userId = customUserDetails.getUser().getId();
		InitialChatListResponseDTO response = chatService.getInitialMessages(userId, chatRoomId);
		return new ApiResponse<>(response);
	}

	// 이전 메세지 더 불러오기
	@GetMapping("/api/chat/previous/chatroom/{chatRoomId}")
	@Operation(summary = "이전 메세지 조회 API", description = "이전 메세지를 조회하는 API 입니다.")
	public ApiResponse<List<ChatResponseDTO>> getPreviousMessages(@PathVariable Long chatRoomId,
		@RequestParam Long messageId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
		Long userId = customUserDetails.getUser().getId();
		List<ChatResponseDTO> messages = chatService.getPreviousMessagesBeforeId(chatRoomId, messageId, userId);
		return new ApiResponse<List<ChatResponseDTO>>(messages);
	}

	// 이후 메세지 더 불러오기
	@GetMapping("/api/chat/next/chatroom/{chatRoomId}")
	@Operation(summary = "이후 메세지 추가 조회 API", description = "이후 메세지를 추가적으로 조회하는 API 입니다.")
	public ApiResponse<List<ChatResponseDTO>> getNextMessages(@PathVariable Long chatRoomId,
		@RequestParam Long messageId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
		Long userId = customUserDetails.getUser().getId();
		List<ChatResponseDTO> messages = chatService.getNextMessagesAfterId(chatRoomId, messageId, userId);
		return new ApiResponse<List<ChatResponseDTO>>(messages);
	}

}
