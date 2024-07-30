package com.goorm.insideout.chat.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goorm.insideout.chat.domain.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {
	// 채팅방의 전체 메시지 개수를 세는 메서드
	long countByChatRoomId(Long chatRoomId);

	// 특정 시간 이후의 메시지 개수를 세는 메서드
	long countByChatRoomIdAndSendTimeAfter(Long chatRoomId, LocalDateTime lastVisitedTime);

	List<Chat> findAllByChatRoomId(Long chatRoomId);

	// 특정 시간 이후의 읽지 않은 메시지 목록을 조회하는 메서드
	@Query("SELECT c FROM Chat c WHERE c.chatRoom.id = :chatRoomId AND c.sendTime > :lastVisitedTime")
	List<Chat> findAllUnreadMessages(@Param("chatRoomId") Long chatRoomId, @Param("lastVisitedTime") LocalDateTime lastVisitedTime);
}

