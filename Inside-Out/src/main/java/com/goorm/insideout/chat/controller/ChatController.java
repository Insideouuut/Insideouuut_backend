package com.goorm.insideout.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import com.goorm.insideout.chat.dto.request.ChatRequestDTO;
import com.goorm.insideout.chat.service.ChatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor

public class ChatController {
	private final ChatService chatService;
	private final SimpMessageSendingOperations messagingTemplate;

	@MessageMapping("/chatRoom/{chatRoomId}")
	public void messageHandler(@DestinationVariable("chatRoomId") Long roomId, ChatRequestDTO message) {
		chatService.createChat(roomId, message);
		messagingTemplate.convertAndSend("/sub/chatRoom/" + roomId, message);
	}

}
