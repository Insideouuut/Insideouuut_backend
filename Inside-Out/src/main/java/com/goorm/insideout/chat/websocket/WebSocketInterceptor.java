package com.goorm.insideout.chat.websocket;

import static com.goorm.insideout.global.exception.ErrorCode.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.goorm.insideout.auth.utils.JWTUtil;
import com.goorm.insideout.global.exception.ModongException;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
@Component
public class WebSocketInterceptor implements ChannelInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketInterceptor.class);

	private final JWTUtil jwtUtil;

	@SneakyThrows
	@Override
	public Message<?> preSend( Message<?> message,  MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

		if (accessor == null) {
			logger.error("MessageHeaderAccessor is null");
			throw ModongException.from(INVALID_STOMP_MESSAGE_HEADER);
		}

		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			String authToken = accessor.getFirstNativeHeader("Authorization");
			logger.info("Authorization Header: {}", authToken);

			if (authToken == null || !authToken.startsWith("Bearer ")) {
				logger.error("Invalid Authorization Header");
				throw ModongException.from(INVALID_AUTH_TOKEN);
			}

			String token = authToken.substring(7);

			if (!jwtUtil.validateToken(token)) {
				logger.error("Invalid JWT token");
				throw ModongException.from(INVALID_AUTH_TOKEN);
			}

			if (jwtUtil.isExpired(token)) {
				logger.error("Expired JWT token");
				throw ModongException.from(EXPIRED_AUTH_TOKEN);
			}

			String userEmail = jwtUtil.getUserEmail(token);

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				userEmail, null, null);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			accessor.setUser(authentication);
		}
		return message;
	}
}