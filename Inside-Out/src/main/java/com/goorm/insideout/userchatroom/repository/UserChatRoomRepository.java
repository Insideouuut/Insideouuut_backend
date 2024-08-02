package com.goorm.insideout.userchatroom.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goorm.insideout.chatroom.domain.ChatRoom;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.userchatroom.domain.UserChatRoom;

public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {
	List<UserChatRoom> findByUserId(Long userId); // User 엔티티의 ID로 조회

	void deleteByUserIdAndChatRoomId(Long userId, Long chatRoomId);

	Long countByChatRoomId(Long chatRoomId);

	Optional<UserChatRoom> findByUserAndChatRoom(User user, ChatRoom chatRoom);

	// 초대되었던 시간 찾기
	@Query("SELECT ucr.invitationTime FROM UserChatRoom ucr WHERE ucr.user.id = :userId AND ucr.chatRoom.id = :chatRoomId")
	LocalDateTime findInvitationTime(@Param("userId") Long userId, @Param("chatRoomId") Long chatRoomId);

	// 입/퇴장 시간 찾기
	@Query("SELECT ucr.configTime FROM UserChatRoom ucr WHERE ucr.user.id = :userId AND ucr.chatRoom.id = :chatRoomId")
	LocalDateTime findConfigTime(@Param("userId") Long userId, @Param("chatRoomId") Long chatRoomId);

}
