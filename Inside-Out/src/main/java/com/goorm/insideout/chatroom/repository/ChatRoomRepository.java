package com.goorm.insideout.chatroom.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goorm.insideout.chatroom.domain.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
	Optional<ChatRoom> findById(long id);
}
