package com.goorm.insideout.chat.service;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.insideout.chat.domain.Chat;
import com.goorm.insideout.chat.dto.request.ChatRequestDTO;
import com.goorm.insideout.chat.repository.ChatRepository;
import com.goorm.insideout.chatroom.domain.ChatRoom;
import com.goorm.insideout.chatroom.repository.ChatRoomRepository;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

	private final ChatRoomRepository chatRoomRepository;
	private final ChatRepository chatRepository;
	private final UserRepository userRepository;
	@Transactional
	public Chat createChat(Long roomId, ChatRequestDTO message, Principal principal) {

		// 채팅을 작성한 작성자 찾기
		User sender = getSender(principal.getName());

		// 채팅방 찾기
		ChatRoom chatRoom = getChatRoom(roomId);

		// 채팅 엔티티 생성

		Chat chat = Chat.builder()
			.content(message.getContent())
			.chatRoom(chatRoom)
			.user(sender)
			.sendTime(LocalDateTime.now())
			.build();
		return chatRepository.save(chat);

	}

	private User getSender(String email) {
		//해당 이메일로 가입한 유저가 존재하는지 확인
		return userRepository.findByEmail(email)
			.orElseThrow(() -> ModongException.from(ErrorCode.USER_NOT_FOUND));
	}

	private ChatRoom getChatRoom(Long roomId) {
		//해당 PK에 맞는 채팅방 검색
		return chatRoomRepository.findById(roomId)
			.orElseThrow(() -> ModongException.from(ErrorCode.CHATROOM_NOT_FOUND));

	}

}
