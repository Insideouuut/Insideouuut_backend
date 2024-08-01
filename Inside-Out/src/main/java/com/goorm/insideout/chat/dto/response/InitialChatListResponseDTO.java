package com.goorm.insideout.chat.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class InitialChatListResponseDTO {
	private List<ChatResponseDTO> readMessages;
	private List<ChatResponseDTO> unreadMessages;
}
