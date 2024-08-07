package com.goorm.insideout.userchatroom.service;

import java.time.LocalDateTime;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.insideout.chat.domain.Chat;
import com.goorm.insideout.chat.dto.response.ChatResponseDTO;
import com.goorm.insideout.chat.repository.ChatRepository;
import com.goorm.insideout.chatroom.domain.ChatRoom;
import com.goorm.insideout.chatroom.repository.ChatRoomRepository;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.user.dto.response.ChatUserResponse;
import com.goorm.insideout.user.dto.response.HostResponse;
import com.goorm.insideout.user.repository.UserRepository;
import com.goorm.insideout.userchatroom.domain.UserChatRoom;
import com.goorm.insideout.userchatroom.repository.UserChatRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserChatRoomService {

	private final UserChatRoomRepository userChatRoomRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatRepository chatRepository;
	private final UserRepository userRepository;
	private final SimpMessageSendingOperations messagingTemplate;

	// 사용자 초대
	@Transactional
	public void inviteUserToChatRoom(Long chatRoomId, User user) {
		ChatRoom chatRoom = findChatRoomById(chatRoomId);
		saveUserChatRoom(chatRoom, user);
		Chat chat = createInviteMessage(chatRoom, user);
		sendInviteMessage(chat);
	}

	// 채팅방 나가기 및 삭제
	@Transactional
	public void userLeaveChatRoom(Long userId, Long chatRoomId) {
		removeUserFromChatRoom(userId, chatRoomId);
		deleteChatRoomIfEmpty(chatRoomId);
	}

	// 채팅방 입장 및 나가기 시 configTime 업데이트
	@Transactional
	public void updateChatRoomConfigTime(Long chatRoomId, User user) {
		updateConfigTime(chatRoomId, user);
	}

	// 유저와 채팅방 관계 저장
	private void saveUserChatRoom(ChatRoom chatRoom, User user) {
		if (userChatRoomRepository.existsByUserAndChatRoom(user, chatRoom)) {
			throw ModongException.from(ErrorCode.CHAT_ALREADY_JOINED);
		}

		UserChatRoom userChatRoom = UserChatRoom.builder()
			.user(user)
			.chatRoom(chatRoom)
			.configTime(null)
			.invitationTime(LocalDateTime.now()) // 초대 시간 설정
			.build();
		userChatRoomRepository.save(userChatRoom);
	}

	// 유저와 채팅방 관계 삭제
	private void removeUserFromChatRoom(Long userId, Long chatRoomId) {
		userChatRoomRepository.deleteByUserIdAndChatRoomId(userId, chatRoomId);
	}

	// 채팅방에 입/퇴장시 시간 기록
	private void updateConfigTime(Long chatRoomId, User user) {
		ChatRoom chatRoom = findChatRoomById(chatRoomId);
		UserChatRoom userChatRoom = userChatRoomRepository.findByUserAndChatRoom(user, chatRoom)
			.orElseThrow(() -> ModongException.from(ErrorCode.CHAT_NOT_AUTHORIZED));
		userChatRoom.setConfigTime(LocalDateTime.now());
		userChatRoomRepository.save(userChatRoom);
	}

	// 채팅방에 사람이 없으면 채팅방 삭제
	private void deleteChatRoomIfEmpty(Long chatRoomId) {
		if (userChatRoomRepository.countByChatRoomId(chatRoomId) == 0) {
			chatRoomRepository.deleteById(chatRoomId); // 사용자 수가 0이면 채팅방 삭제
		}
	}

	// 시스템의 초대메세지 생성
	private Chat createInviteMessage(ChatRoom chatRoom, User user) {
		String enterMessage = user.getName() + "님이 채팅방에 초대되었습니다.";
		User systemUser = userRepository.findByEmail("system@insideout.com")
			.orElseThrow(() -> ModongException.from(ErrorCode.USER_NOT_FOUND));
		return Chat.builder()
			.content(enterMessage)
			.sendTime(LocalDateTime.now())
			.chatRoom(chatRoom)
			.user(systemUser)
			.build();
	}

	// 메세지 저장 및 실시간 전송
	private void sendInviteMessage(Chat chat) {
		chatRepository.save(chat);
		ChatResponseDTO chatResponseDTO = convertToChatResponseDTO(chat);
		messagingTemplate.convertAndSend("/sub/chatRoom/" + chat.getChatRoom().getId(), chatResponseDTO);
	}

	// dto로 치환
	private ChatResponseDTO convertToChatResponseDTO(Chat chat) {
		return ChatResponseDTO.builder()
			.content(chat.getContent())
			.sendTime(chat.getSendTime())
			.sender(ChatUserResponse.from(chat.getUser()))
			.build();
	}

	// 채팅방 찾기 및 유효성 검사
	private ChatRoom findChatRoomById(Long chatRoomId) {
		return chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> ModongException.from(ErrorCode.CHATROOM_NOT_FOUND));
	}

}
