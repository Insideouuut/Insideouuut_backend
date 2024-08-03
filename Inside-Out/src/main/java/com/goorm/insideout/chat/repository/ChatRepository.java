package com.goorm.insideout.chat.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goorm.insideout.chat.domain.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {

	// 특정 시간 이후의 메시지 개수를 세는 메서드
	long countByChatRoomIdAndSendTimeAfter(Long chatRoomId, LocalDateTime time);

	List<Chat> findAllByChatRoomId(Long chatRoomId);

	// 특정 시간 이후의 읽지 않은 메시지 목록을 조회하는 메서드
	@Query("SELECT c FROM Chat c WHERE c.chatRoom.id = :chatRoomId AND c.sendTime > :lastVisitedTime")
	List<Chat> findAllUnreadMessages(@Param("chatRoomId") Long chatRoomId,
		@Param("lastVisitedTime") LocalDateTime lastVisitedTime);

	// 읽지 않은 메시지 조회
	@Query(value = "SELECT * FROM CHATS c WHERE c.chat_room_id = :chatRoomId " +
		"AND (:lastVisitedTime IS NULL OR c.send_time > :lastVisitedTime) " +
		"AND c.send_time > :invitationTime " +
		"ORDER BY c.send_time ASC LIMIT 30", nativeQuery = true)
	List<Chat> findUnreadMessages(@Param("chatRoomId") Long chatRoomId,
		@Param("lastVisitedTime") LocalDateTime lastVisitedTime,
		@Param("invitationTime") LocalDateTime invitationTime);

	// 읽은 메시지 조회
	@Query(value = "SELECT * FROM CHATS c WHERE c.chat_room_id = :chatRoomId " +
		"AND c.send_time < :lastVisitedTime " +
		"AND c.send_time > :invitationTime " +
		"ORDER BY c.send_time DESC LIMIT 30", nativeQuery = true)
	List<Chat> findPreviousMessages(@Param("chatRoomId") Long chatRoomId,
		@Param("lastVisitedTime") LocalDateTime lastVisitedTime,
		@Param("invitationTime") LocalDateTime invitationTime);

	// 처음 메시지 ID 이전의 메시지 조회
	@Query(value = "SELECT * FROM CHATS c WHERE c.chat_room_id = :chatRoomId " +
		"AND c.chat_id < :firstMessageId " +
		"AND c.send_time > :invitationTime " +
		"ORDER BY c.send_time DESC LIMIT 30", nativeQuery = true)
	List<Chat> findPreviousMessagesBeforeId(@Param("chatRoomId") Long chatRoomId,
		@Param("firstMessageId") Long firstMessageId,
		@Param("invitationTime") LocalDateTime invitationTime);

	// 마지막 메시지 ID 이후의 메시지 조회
	@Query(value = "SELECT * FROM CHATS c WHERE c.chat_room_id = :chatRoomId " +
		"AND c.send_time > :invitationTime " +
		"AND c.chat_id > :lastMessageId " +
		"ORDER BY c.send_time ASC LIMIT 30", nativeQuery = true)
	List<Chat> findNextMessagesAfterId(@Param("chatRoomId") Long chatRoomId,
		@Param("lastMessageId") Long lastMessageId,
		@Param("invitationTime") LocalDateTime invitationTime);
}