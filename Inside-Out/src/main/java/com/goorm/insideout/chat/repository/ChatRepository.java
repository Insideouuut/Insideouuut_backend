
package com.goorm.insideout.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.goorm.insideout.chat.domain.Chat;
import com.goorm.insideout.chat.repository.custom.ChatQueryDslRepository;

public interface ChatRepository extends JpaRepository<Chat, Long>, ChatQueryDslRepository {

}
