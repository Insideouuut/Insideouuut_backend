package com.goorm.insideout.chat.dto.response;

import java.time.LocalDateTime;
import java.util.Objects;

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
	private String sender;
}
