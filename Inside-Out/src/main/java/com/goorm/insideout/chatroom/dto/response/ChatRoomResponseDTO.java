package com.goorm.insideout.chatroom.dto.response;

import java.time.LocalDateTime;

import com.goorm.insideout.chatroom.domain.ChatRoom;
import com.goorm.insideout.chatroom.domain.ChatRoomType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatRoomResponseDTO {

	private Long chatRoomId;

	private Long associatedId;

	private String title;

	private ChatRoomType type;

	private String lastMessageContent;

	private LocalDateTime lastMessageTime;

	private Long userCount;

	private Long unreadMessageCnt;

	public static ChatRoomResponseDTO of(ChatRoom chatRoom, Long userCount,Long unreadMessageCnt) {
		return ChatRoomResponseDTO.builder()
			.chatRoomId(chatRoom.getId())
			.associatedId(chatRoom.getAssociatedId())
			.title(chatRoom.getTitle())
			.type(chatRoom.getType())
			.lastMessageContent(chatRoom.getLastMessageContent())
			.lastMessageTime(chatRoom.getLastMessageTime())
			.userCount(userCount)
			.unreadMessageCnt(unreadMessageCnt)
			.build();
	}

}
