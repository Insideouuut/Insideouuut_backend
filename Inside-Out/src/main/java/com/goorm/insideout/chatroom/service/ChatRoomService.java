package com.goorm.insideout.chatroom.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.insideout.chat.repository.ChatRepository;
import com.goorm.insideout.chatroom.domain.ChatRoom;
import com.goorm.insideout.chatroom.domain.ChatRoomType;
import com.goorm.insideout.chatroom.dto.response.ChatRoomResponseDTO;
import com.goorm.insideout.chatroom.repository.ChatRoomRepository;
import com.goorm.insideout.userchatroom.repository.UserChatRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

	private final UserChatRoomRepository userChatRoomRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatRepository chatRepository;

	// 채팅방 생성
	@Transactional
	public ChatRoom createChatRoom(Long associatedId, String title, ChatRoomType type) {
		ChatRoom chatRoom = ChatRoom.builder()
			.associatedId(associatedId)
			.title(title)
			.type(type)
			.build();
		return chatRoomRepository.save(chatRoom);
	}

	// 채팅방 목록 조회
	@Transactional(readOnly = true)
	public List<ChatRoomResponseDTO> getChatRoomsByUserId(Long userId) {
		return getChatRoomsByUserIdAndType(userId, null);
	}

	// 클럽 채팅방 조회
	@Transactional(readOnly = true)
	public List<ChatRoomResponseDTO> getClubRoomsByUserId(Long userId) {
		return getChatRoomsByUserIdAndType(userId, ChatRoomType.CLUB);
	}

	// 미팅 채팅방 조회
	@Transactional(readOnly = true)
	public List<ChatRoomResponseDTO> getMeetingRoomsByUserId(Long userId) {
		return getChatRoomsByUserIdAndType(userId, ChatRoomType.MEETING);
	}

	@Transactional(readOnly = true)
	public List<ChatRoomResponseDTO> getChatRoomsByUserIdAndType(Long userId, ChatRoomType type) {
		List<Long> chatRoomIds = getChatRoomIdsByUserId(userId);

		Map<Long, Long> userCountByChatRoomId = getUserCountByChatRoomId(chatRoomIds);
		Map<Long, Long> unreadMessageCountByChatRoomId = getUnreadMessageCountByChatRoomId(userId, chatRoomIds);

		return chatRoomRepository.findAllById(chatRoomIds).stream()
			.filter(chatRoom -> type == null || type.equals(chatRoom.getType()))
			.map(chatRoom -> convertToChatRoomResponseDTO(
				chatRoom,
				userCountByChatRoomId.getOrDefault(chatRoom.getId(), 0L),
				unreadMessageCountByChatRoomId.getOrDefault(chatRoom.getId(), 0L)))
			.collect(Collectors.toList());
	}

	// 회원이 속한 채팅방 다 가져오기
	private List<Long> getChatRoomIdsByUserId(Long userId) {
		return userChatRoomRepository.findByUserId(userId).stream()
			.map(userChatRoom -> userChatRoom.getChatRoom().getId())
			.distinct()
			.collect(Collectors.toList());
	}

	// 채팅방에 유저수 구하기
	private Map<Long, Long> getUserCountByChatRoomId(List<Long> chatRoomIds) {
		return chatRoomIds.stream()
			.collect(Collectors.toMap(chatRoomId -> chatRoomId, userChatRoomRepository::countByChatRoomId));
	}

	// 채팅방에 안읽은 메세지 수 구하기
	private Map<Long, Long> getUnreadMessageCountByChatRoomId(Long userId, List<Long> chatRoomIds) {
		return chatRoomIds.stream()
			.collect(Collectors.toMap(chatRoomId -> chatRoomId, chatRoomId -> {
				// 초대 시간 조회
				LocalDateTime invitationTime = userChatRoomRepository.findInvitationTime(userId, chatRoomId);

				// 마지막 방문 시간 조회
				LocalDateTime lastVisitedTime = userChatRoomRepository.findConfigTime(userId, chatRoomId);

				// 초대 시간 이후의 메시지 카운트
				if (lastVisitedTime == null) {
					// 사용자가 아직 방문하지 않은 경우: 초대 이후의 모든 메시지 카운트
					return chatRepository.countByChatRoomIdAndSendTimeAfter(chatRoomId, invitationTime);
				}
				return chatRepository.countByChatRoomIdAndSendTimeAfter(chatRoomId, lastVisitedTime);
			}));
	}

	// ChatRoom을 ChatRoomResponseDTO로 변환
	private ChatRoomResponseDTO convertToChatRoomResponseDTO(ChatRoom chatRoom, Long userCount,
		Long unreadMessageCount) {
		return ChatRoomResponseDTO.builder()
			.title(chatRoom.getTitle())
			.type(chatRoom.getType())
			.lastMessageContent(chatRoom.getLastMessageContent())
			.lastMessageTime(chatRoom.getLastMessageTime())
			.userCount(userCount)
			.unreadMessageCnt(unreadMessageCount)
			.build();
	}
}
