package com.goorm.insideout.chat.websocket;

import java.nio.charset.StandardCharsets;

import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompConversionException;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import com.goorm.insideout.global.exception.ModongException;

@Component
public class WebSocketErrorHandler extends StompSubProtocolErrorHandler {
	public WebSocketErrorHandler() {
		super();
	}

	@Override
	public Message<byte[]> handleClientMessageProcessingError(@Nullable Message<byte[]> clientMessage,
		@Nullable Throwable ex) {
		if (ex instanceof MessageDeliveryException) {
			ex = ex.getCause();
		}
		if (ex instanceof ModongException) {
			return handleModongException(clientMessage, (ModongException)ex);
		}
		if (ex instanceof StompConversionException) {
			return handleStompConversionException(clientMessage, (StompConversionException)ex);
		}
		return super.handleClientMessageProcessingError(clientMessage, ex);
	}

	// ModongException 처리
	private Message<byte[]> handleModongException(Message<byte[]> clientMessage, ModongException ex) {
		String errorMessage = ex.GetMessage();
		return createErrorMessage(errorMessage);
	}

	private Message<byte[]> handleStompConversionException(Message<byte[]> clientMessage, StompConversionException ex) {
		String errorMessage = "Invalid STOMP header format: " + ex.getMessage();
		return createErrorMessage(errorMessage);
	}

	// 에러 메시지 생성
	private Message<byte[]> createErrorMessage(String errorMessage) {
		StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
		accessor.setMessage(errorMessage);
		accessor.setLeaveMutable(true);

		return MessageBuilder.createMessage(errorMessage.getBytes(StandardCharsets.UTF_8),
			accessor.getMessageHeaders());
	}
}