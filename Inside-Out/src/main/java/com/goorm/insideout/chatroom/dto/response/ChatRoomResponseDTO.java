package com.goorm.insideout.chatroom.dto.response;

import java.time.LocalDateTime;

import com.goorm.insideout.chatroom.domain.ChatRoomType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatRoomResponseDTO {

	private String title;

	private ChatRoomType type;

	private String lastMessageContent;

	private LocalDateTime lastMessageTime;

	private Long userCount;

	private Long unreadMessageCnt;
}