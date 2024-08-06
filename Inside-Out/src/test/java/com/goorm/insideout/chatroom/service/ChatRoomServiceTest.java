package com.goorm.insideout.chatroom.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.goorm.insideout.chat.repository.ChatRepository;
import com.goorm.insideout.chatroom.domain.ChatRoom;
import com.goorm.insideout.chatroom.domain.ChatRoomType;
import com.goorm.insideout.chatroom.dto.response.ChatRoomResponseDTO;
import com.goorm.insideout.chatroom.repository.ChatRoomRepository;
import com.goorm.insideout.userchatroom.domain.UserChatRoom;
import com.goorm.insideout.userchatroom.repository.UserChatRoomRepository;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {

	@Mock
	private ChatRoomRepository chatRoomRepository;

	@Mock
	private ChatRepository chatRepository;

	@Mock
	private UserChatRoomRepository userChatRoomRepository;

	@InjectMocks
	private ChatRoomService chatRoomService;

	private ChatRoom testChatRoom;
	private UserChatRoom testUserChatRoom;
	private Long userId;
	private Long chatRoomId;

	@BeforeEach
	void setUp() {
		userId = 1L;
		chatRoomId = 1L;

		testChatRoom = ChatRoom.builder()
			.id(chatRoomId)
			.associatedId(1L)
			.title("Test Chat Room")
			.type(ChatRoomType.CLUB)
			.build();

		testUserChatRoom = UserChatRoom.builder()
			.chatRoom(testChatRoom)
			.build();
	}

	@Test
	void testCreateChatRoom() {
		// Given
		ChatRoom chatRoom = ChatRoom.builder()
			.id(chatRoomId)
			.associatedId(1L)
			.title("Test Chat Room")
			.type(ChatRoomType.CLUB)
			.build();

		// Mock setup
		when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(chatRoom);

		// When
		ChatRoom createdChatRoom = chatRoomService.createChatRoom(1L, "Test Chat Room", ChatRoomType.CLUB);

		// Then
		assertNotNull(createdChatRoom, "The created chat room should not be null");
		assertEquals(chatRoomId, createdChatRoom.getId(), "The chat room ID should match");
		assertEquals("Test Chat Room", createdChatRoom.getTitle(), "The chat room title should match");
		assertEquals(ChatRoomType.CLUB, createdChatRoom.getType(), "The chat room type should match");
	}

	@Test
	void testGetChatRoomsByUserId() {
		// Given
		Long userId = 1L;
		Long chatRoomId = 1L;

		ChatRoom chatRoom = ChatRoom.builder()
			.id(chatRoomId)
			.title("Chat Room")
			.type(ChatRoomType.CLUB)  // Ensure the type is set
			.lastMessageContent("Last message")
			.lastMessageTime(LocalDateTime.now())
			.build();

		List<UserChatRoom> userChatRooms = Collections.singletonList(UserChatRoom.builder()
			.chatRoom(chatRoom)
			.build());

		// Mock setup
		when(userChatRoomRepository.findByUserId(userId)).thenReturn(userChatRooms);
		when(chatRoomRepository.findAllById(Collections.singletonList(chatRoomId)))
			.thenReturn(Collections.singletonList(chatRoom));
		when(userChatRoomRepository.countByChatRoomId(chatRoomId)).thenReturn(1L);

		// When
		List<ChatRoomResponseDTO> chatRooms = chatRoomService.getChatRoomsByUserId(userId);

		// Then
		assertNotNull(chatRooms);
		assertEquals(1, chatRooms.size());

		ChatRoomResponseDTO resultDTO = chatRooms.get(0);
		assertEquals("Chat Room", resultDTO.getTitle());
		assertEquals(ChatRoomType.CLUB, resultDTO.getType());  // Ensure the type is checked
		assertEquals("Last message", resultDTO.getLastMessageContent());
		assertEquals(1L, resultDTO.getUserCount());
		assertEquals(0L, resultDTO.getUnreadMessageCnt());
	}

	@Test
	void testGetClubRoomsByUserId() {
		when(userChatRoomRepository.findByUserId(userId)).thenReturn(Collections.singletonList(testUserChatRoom));
		when(chatRoomRepository.findAllById(Collections.singletonList(chatRoomId)))
			.thenReturn(Collections.singletonList(testChatRoom));

		List<ChatRoomResponseDTO> clubRooms = chatRoomService.getClubRoomsByUserId(userId);

		assertNotNull(clubRooms);
		assertEquals(1, clubRooms.size());
		assertEquals(ChatRoomType.CLUB, clubRooms.get(0).getType());
	}

	@Test
	void testGetMeetingRoomsByUserId() {

		ChatRoom meetingRoom = ChatRoom.builder().id(chatRoomId).title("Meeting Room").type(ChatRoomType.MEETING).build();
		when(userChatRoomRepository.findByUserId(userId)).thenReturn(Collections.singletonList(testUserChatRoom));
		when(chatRoomRepository.findAllById(Collections.singletonList(chatRoomId)))
			.thenReturn(Collections.singletonList(meetingRoom));


		List<ChatRoomResponseDTO> meetingRooms = chatRoomService.getMeetingRoomsByUserId(userId);


		assertNotNull(meetingRooms);
		assertEquals(1, meetingRooms.size());
		assertEquals(ChatRoomType.MEETING, meetingRooms.get(0).getType());
	}

}
