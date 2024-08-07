package com.goorm.insideout.userchatroom.service;

import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.goorm.insideout.chat.domain.Chat;
import com.goorm.insideout.chat.dto.response.ChatResponseDTO;
import com.goorm.insideout.chat.repository.ChatRepository;
import com.goorm.insideout.chatroom.domain.ChatRoom;
import com.goorm.insideout.chatroom.repository.ChatRoomRepository;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.user.repository.UserRepository;
import com.goorm.insideout.userchatroom.domain.UserChatRoom;
import com.goorm.insideout.userchatroom.repository.UserChatRoomRepository;

@ExtendWith(MockitoExtension.class)
public class UserChatRoomServiceTest {

	@InjectMocks
	private UserChatRoomService userChatRoomService;

	@Mock
	private SimpMessageSendingOperations messagingTemplate;

	@Mock
	private UserChatRoomRepository userChatRoomRepository;

	@Mock
	private ChatRepository chatRepository;

	@Mock
	private ChatRoomRepository chatRoomRepository;

	@Mock
	private UserRepository userRepository;

	@Test
	public void testInviteUserToChatRoom() {
		Long chatRoomId = 1L;
		Long userId = 1L;

		ChatRoom chatRoom = ChatRoom.builder().id(chatRoomId).build();
		User user = User.builder().id(userId).name("Test User").build();
		user.initDefaultProfileImage();
		User systemUser = User.builder().email("system@insideout.com").build();
		systemUser.initDefaultProfileImage();

		when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(chatRoom));
		when(userRepository.findByEmail("system@insideout.com")).thenReturn(Optional.of(systemUser));

		userChatRoomService.inviteUserToChatRoom(chatRoomId, user);

		// Verify UserChatRoom saving
		verify(userChatRoomRepository, times(1)).save(any(UserChatRoom.class));

		// Verify Chat saving
		verify(chatRepository, times(1)).save(any(Chat.class));

		// Verify message sending
		verify(messagingTemplate, times(1)).convertAndSend(any(String.class), any(ChatResponseDTO.class));
	}

	@Test
	public void testUserLeaveChatRoom() {
		Long userId = 1L;
		Long chatRoomId = 1L;

		when(userChatRoomRepository.countByChatRoomId(chatRoomId)).thenReturn(1L);

		userChatRoomService.userLeaveChatRoom(userId, chatRoomId);

		// Verify user removal from chat room
		verify(userChatRoomRepository, times(1)).deleteByUserIdAndChatRoomId(userId, chatRoomId);

		// Verify chat room deletion not called
		verify(chatRoomRepository, never()).deleteById(chatRoomId);
	}

	@Test
	public void testUserLeaveChatRoomAndDelete() {
		Long userId = 1L;
		Long chatRoomId = 1L;

		when(userChatRoomRepository.countByChatRoomId(chatRoomId)).thenReturn(0L);

		userChatRoomService.userLeaveChatRoom(userId, chatRoomId);

		// Verify user removal from chat room
		verify(userChatRoomRepository, times(1)).deleteByUserIdAndChatRoomId(userId, chatRoomId);

		// Verify chat room deletion
		verify(chatRoomRepository, times(1)).deleteById(chatRoomId);
	}

	@Test
	public void testUpdateChatRoomConfigTime() {
		Long chatRoomId = 1L;
		Long userId = 1L;

		User user = User.builder().id(userId).build();
		user.initDefaultProfileImage();
		ChatRoom chatRoom = ChatRoom.builder().id(chatRoomId).build();

		when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(chatRoom));
		when(userChatRoomRepository.findByUserAndChatRoom(user, chatRoom))
			.thenReturn(Optional.of(UserChatRoom.builder().build()));

		userChatRoomService.updateChatRoomConfigTime(chatRoomId, user);

		// Verify configTime update
		verify(userChatRoomRepository, times(1)).save(any(UserChatRoom.class));
	}
}
