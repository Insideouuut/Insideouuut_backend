package com.goorm.insideout.chat.dto.response;

import java.time.LocalDateTime;

import com.goorm.insideout.chat.domain.Chat;
import com.goorm.insideout.user.dto.response.HostResponse;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class ChatResponseDTO {
	private Long id;
	private String content;
	private LocalDateTime sendTime;
	private HostResponse sender;

	public static ChatResponseDTO of(Chat chat) {
		return ChatResponseDTO.builder()
			.id(chat.getId())
			.content(chat.getContent())
			.sendTime(chat.getSendTime())
			.sender(HostResponse.fromEntity(chat.getUser()))
			.build();
	}

}
