package com.goorm.insideout.chat.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.insideout.chat.domain.Chat;
import com.goorm.insideout.chat.dto.request.ChatRequestDTO;
import com.goorm.insideout.chat.dto.response.ChatResponseDTO;
import com.goorm.insideout.chat.dto.response.InitialChatListResponseDTO;
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
		validateMessageContent(message.getContent());

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

	// 빈값 에러 처리
	private void validateMessageContent(String content) {
		if (content == null || content.trim().isEmpty()) {
			throw ModongException.from(ErrorCode.CHAT_NOT_EMPTY);
		}
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
			.id(chat.getId())
			.content(chat.getContent())
			.sendTime(chat.getSendTime())
			.sender(chat.getUser().getName())
			.build();
	}

	// 초기 메시지 조회
	@Transactional(readOnly = true)
	public InitialChatListResponseDTO getInitialMessages(Long userId, Long chatRoomId) {
		LocalDateTime lastVisitedTime = userChatRoomRepository.findConfigTime(userId, chatRoomId);
		LocalDateTime invitationTime = userChatRoomRepository.findInvitationTime(userId, chatRoomId);

		// 읽지 않은 메시지 가져오기
		List<Chat> unreadChats = chatRepository.findUnreadMessages(chatRoomId, lastVisitedTime, invitationTime);
		List<ChatResponseDTO> unreadChatDTOs = unreadChats.stream()
			.map(this::convertToChatResponseDTO)
			.collect(Collectors.toList());

		// 읽은 메시지 가져오기
		List<Chat> readChats = chatRepository.findPreviousMessages(chatRoomId, lastVisitedTime, invitationTime);

		// 읽은 메세지 결과를 시간순으로 정렬
		List<Chat> sortedReadChats = readChats.stream()
			.sorted(Comparator.comparing(Chat::getSendTime))
			.toList();

		// dto로 치환
		List<ChatResponseDTO> readChatDTOs = sortedReadChats.stream()
			.map(this::convertToChatResponseDTO)
			.collect(Collectors.toList());

		return InitialChatListResponseDTO.builder()
			.readMessages(readChatDTOs)
			.unreadMessages(unreadChatDTOs)
			.build();
	}

	// 처음 메시지 ID 이전의 메시지 가져오기
	@Transactional(readOnly = true)
	public List<ChatResponseDTO> getPreviousMessagesBeforeId(Long chatRoomId, Long firstMessageId, Long userId) {
		LocalDateTime invitationTime = userChatRoomRepository.findInvitationTime(userId, chatRoomId);

		// 채팅 메시지 조회
		List<Chat> previousChats = chatRepository.findPreviousMessagesBeforeId(chatRoomId, firstMessageId,
			invitationTime);

		// 조회된 메세지 결과를 시간순으로 정렬
		List<Chat> sortedReadChats = previousChats.stream()
			.sorted(Comparator.comparing(Chat::getSendTime))
			.toList();

		return sortedReadChats.stream()
			.map(this::convertToChatResponseDTO)
			.collect(Collectors.toList());
	}

	// 마지막 메시지 ID 이후의 메시지 가져오기
	@Transactional(readOnly = true)
	public List<ChatResponseDTO> getNextMessagesAfterId(Long chatRoomId, Long lastMessageId, Long userId) {
		LocalDateTime invitationTime = userChatRoomRepository.findInvitationTime(userId, chatRoomId);

		// 채팅 메시지 조회
		List<Chat> nextChats = chatRepository.findNextMessagesAfterId(chatRoomId, lastMessageId, invitationTime);
		return nextChats.stream()
			.map(this::convertToChatResponseDTO)
			.collect(Collectors.toList());
	}

}
