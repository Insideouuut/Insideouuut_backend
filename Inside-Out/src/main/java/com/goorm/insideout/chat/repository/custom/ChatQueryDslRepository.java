package com.goorm.insideout.chat.repository.custom;

import java.time.LocalDateTime;
import java.util.List;

import com.goorm.insideout.chat.domain.Chat;

public interface ChatQueryDslRepository {
	// 특정 시간 이후의 메시지 개수를 세는 메서드
	long countByChatRoomIdAndSendTimeAfter(Long chatRoomId, LocalDateTime time);
	// 읽지 않은 메시지 조회
	List<Chat> findUnreadMessages(Long chatRoomId, LocalDateTime lastVisitedTime, LocalDateTime invitationTime);
	// 읽은 메시지 조회
	List<Chat> findPreviousMessages(Long chatRoomId, LocalDateTime lastVisitedTime, LocalDateTime invitationTime);
	// 처음 메시지 ID 이전의 메시지 조회
	List<Chat> findPreviousMessagesBeforeId(Long chatRoomId, Long firstMessageId, LocalDateTime invitationTime);
	// 마지막 메시지 ID 이후의 메시지 조회
	List<Chat> findNextMessagesAfterId(Long chatRoomId, Long lastMessageId, LocalDateTime configTime);

}
