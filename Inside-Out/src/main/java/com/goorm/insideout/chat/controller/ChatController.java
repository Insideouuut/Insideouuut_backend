package com.goorm.insideout.chat.controller;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.goorm.insideout.chat.domain.Chat;
import com.goorm.insideout.chat.dto.request.ChatRequestDTO;
import com.goorm.insideout.chat.dto.response.ChatResponseDTO;
import com.goorm.insideout.chat.service.ChatService;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatController {
	private final ChatService chatService;
	private final SimpMessageSendingOperations messagingTemplate;

	@MessageMapping("/chatRoom/{chatRoomId}")
	public ApiResponse messageHandler(@DestinationVariable("chatRoomId") Long roomId,
		@RequestBody ChatRequestDTO chatRequestDTO, Principal principal) {

		Chat chat = chatService.createChat(roomId, chatRequestDTO, principal);
		ChatResponseDTO chatResponseDTO = ChatResponseDTO.builder()
			.content(chat.getContent())
			.sender(chat.getUser().getName())
			.sendTime(chat.getSendTime()).build();
		messagingTemplate.convertAndSend("/sub/chatRoom/" + roomId, chatResponseDTO);
		return new ApiResponse<>(ErrorCode.REQUEST_OK);

	}
}
