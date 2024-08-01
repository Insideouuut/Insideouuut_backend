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
public class ChatResponseDTO {
	private Long id;
	private String content;
	private LocalDateTime sendTime;
	private String sender;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ChatResponseDTO that = (ChatResponseDTO)o;
		return Objects.equals(content, that.content) &&
			Objects.equals(sendTime, that.sendTime) &&
			Objects.equals(sender, that.sender);
	}

	@Override
	public int hashCode() {
		return Objects.hash(content, sendTime, sender);
	}
}
