package com.goorm.insideout.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goorm.insideout.chat.domain.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {

}
