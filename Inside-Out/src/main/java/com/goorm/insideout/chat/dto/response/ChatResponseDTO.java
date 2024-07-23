package com.goorm.insideout.chat.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatResponseDTO {
	private String content;
	private LocalDateTime sendTime;
	private String sender;

}
