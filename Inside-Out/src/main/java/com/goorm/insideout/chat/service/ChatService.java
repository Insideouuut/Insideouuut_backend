package com.goorm.insideout.chat.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.goorm.insideout.chat.domain.Chat;
import com.goorm.insideout.chat.dto.request.ChatRequestDTO;
import com.goorm.insideout.chat.repository.ChatRepository;
import com.goorm.insideout.chatroom.domain.ChatRoom;
import com.goorm.insideout.chatroom.repository.ChatRoomRepository;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

	private final ChatRoomRepository chatRoomRepository;
	private final ChatRepository chatRepository;
	private final UserRepository userRepository;

	public void createChat(Long roomId, ChatRequestDTO message) {

		// 채팅을 작성한 작성자 찾기
		User sender = userRepository.findById(message.getUserId())
			.orElseThrow(() -> new RuntimeException("User not found"));

		// 채팅방 찾기
		ChatRoom chatRoom = chatRoomRepository.findById(roomId)
			.orElseThrow(() -> new RuntimeException("Chat room not found"));

		// 채팅 엔티티 생성
		LocalDateTime now = LocalDateTime.now();
		Chat chat = Chat.builder()
			.content(message.getContent())
			.chatRoom(chatRoom)
			.user(sender)
			.sendTime(now)
			.build();
		chatRepository.save(chat);

	}
}
