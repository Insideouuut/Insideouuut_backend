package com.goorm.insideout.chat.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.insideout.chat.domain.Chat;
import com.goorm.insideout.chat.dto.request.ChatRequestDTO;
import com.goorm.insideout.chat.dto.response.ChatResponseDTO;
import com.goorm.insideout.chat.repository.ChatRepository;
import com.goorm.insideout.chatroom.domain.ChatRoom;
import com.goorm.insideout.chatroom.repository.ChatRoomRepository;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.user.repository.UserRepository;
import com.goorm.insideout.userchatroom.repository.UserChatRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

	private final ChatRepository chatRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final UserChatRoomRepository userChatRoomRepository;
	private final UserRepository userRepository;

	// 채팅 생성
	@Transactional
	public Chat createChat(Long roomId, ChatRequestDTO message, String email) {
		ChatRoom chatRoom = getChatRoom(roomId);

		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> ModongException.from(ErrorCode.CHATROOM_NOT_FOUND));

		Chat chat = Chat.builder()
			.content(message.getContent())
			.chatRoom(chatRoom)
			.user(user)
			.sendTime(LocalDateTime.now())
			.build();

		updateChatRoomLastMessage(chatRoom, message.getContent());
		return chatRepository.save(chat);
	}

	// 읽지 않은 메시지 조회
	@Transactional(readOnly = true)
	public List<ChatResponseDTO> getUnreadMessages(Long userId, Long chatRoomId) {
		LocalDateTime lastVisitedTime = userChatRoomRepository.findConfigTime(userId, chatRoomId);
		List<Chat> chats = (lastVisitedTime == null) ?
			chatRepository.findAllByChatRoomId(chatRoomId) :
			chatRepository.findAllUnreadMessages(chatRoomId, lastVisitedTime);

		return chats.stream()
			.map(this::convertToChatResponseDTO)
			.collect(Collectors.toList());
	}

	// 채팅방 가져오기
	private ChatRoom getChatRoom(Long roomId) {
		return chatRoomRepository.findById(roomId)
			.orElseThrow(() -> ModongException.from(ErrorCode.CHATROOM_NOT_FOUND));
	}

	// 채팅방 마지막 메시지 업데이트
	private void updateChatRoomLastMessage(ChatRoom chatRoom, String messageContent) {
		chatRoom.setLastMessageContent(messageContent);
		chatRoom.setLastMessageTime(LocalDateTime.now());
		chatRoomRepository.save(chatRoom);
	}

	// Chat 엔티티를 ChatResponseDTO로 변환
	private ChatResponseDTO convertToChatResponseDTO(Chat chat) {
		return ChatResponseDTO.builder()
			.content(chat.getContent())
			.sendTime(chat.getSendTime())
			.sender(chat.getUser().getName())
			.build();
	}

}
