package com.goorm.insideout.chat.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.insideout.chat.domain.Chat;
import com.goorm.insideout.chat.dto.request.ChatRequestDTO;
import com.goorm.insideout.chat.dto.response.ChatResponseDTO;
import com.goorm.insideout.chat.dto.response.InitialChatListResponseDTO;
import com.goorm.insideout.chat.repository.ChatRepository;
import com.goorm.insideout.chatroom.domain.ChatRoom;
import com.goorm.insideout.chatroom.repository.ChatRoomRepository;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.user.repository.UserRepository;
import com.goorm.insideout.userchatroom.repository.UserChatRoomRepository;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {

	@Mock
	private ChatRoomRepository chatRoomRepository;

	@Mock
	private ChatRepository chatRepository;

	@Mock
	private UserChatRoomRepository userChatRoomRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private ChatService chatService;

	private User testUser;
	private ChatRoom testChatRoom;
	private ChatRequestDTO chatRequestDTO;
	private Chat testChat;
	private LocalDateTime lastVisitedTime;

	@BeforeEach
	public void setUp() {
		testUser = User.builder()
			.id(1L)
			.email("test@example.com")
			.password("password")
			.name("Test User")
			.build();

		testChatRoom = ChatRoom.builder()
			.id(1L)
			.title("Test Chat Room")
			.build();

		chatRequestDTO = new ChatRequestDTO("Hello World");

		testChat = Chat.builder()
			.id(1L)
			.content("Hello World")
			.chatRoom(testChatRoom)
			.user(testUser)
			.sendTime(LocalDateTime.now())
			.build();

		lastVisitedTime = LocalDateTime.now().minusDays(1);
	}

	@Test
	public void testCreateChat() {
		// Mock 설정
		when(chatRoomRepository.findById(testChatRoom.getId())).thenReturn(Optional.of(testChatRoom));
		when(chatRepository.save(any(Chat.class))).thenReturn(testChat);
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));
		// Call the service method
		Chat createdChat = chatService.createChat(testChatRoom.getId(), chatRequestDTO, testUser.getEmail());

		// Assertions
		assertNotNull(createdChat);
		assertEquals(testChat.getContent(), createdChat.getContent());
		assertEquals(testChat.getUser(), createdChat.getUser());
		assertEquals(testChat.getChatRoom(), createdChat.getChatRoom());

		// Verify interactions
		verify(chatRoomRepository).findById(testChatRoom.getId());
		verify(chatRepository).save(any(Chat.class));
	}

	@Test
	public void testCreateChat_ChatRoomNotFound() {
		when(chatRoomRepository.findById(testChatRoom.getId())).thenReturn(Optional.empty());

		ModongException thrown = assertThrows(ModongException.class, () -> {
			chatService.createChat(testChatRoom.getId(), chatRequestDTO, testUser.getEmail());
		});

		assertEquals(ErrorCode.CHATROOM_NOT_FOUND, thrown.getErrorCode());
	}

	@Test
	public void testGetUnreadMessages_WithLastVisitedTime() {
		List<Chat> unreadChats = Arrays.asList(testChat);

		List<ChatResponseDTO> expectedResponse = unreadChats.stream()
			.map(chat -> ChatResponseDTO.builder()
				.content(chat.getContent())
				.sendTime(chat.getSendTime())
				.sender(chat.getUser().getName())
				.build())
			.collect(Collectors.toList());

		when(userChatRoomRepository.findConfigTime(testUser.getId(), testChatRoom.getId())).thenReturn(lastVisitedTime);
		when(chatRepository.findAllUnreadMessages(testChatRoom.getId(), lastVisitedTime)).thenReturn(unreadChats);

		List<ChatResponseDTO> result = chatService.getUnreadMessages(testUser.getId(), testChatRoom.getId());

		assertEquals(expectedResponse, result);
	}

	@Test
	public void testGetUnreadMessages_WithoutLastVisitedTime() {
		List<Chat> allChats = Arrays.asList(testChat);

		List<ChatResponseDTO> expectedResponse = allChats.stream()
			.map(chat -> ChatResponseDTO.builder()
				.content(chat.getContent())
				.sendTime(chat.getSendTime())
				.sender(chat.getUser().getName())
				.build())
			.collect(Collectors.toList());

		when(userChatRoomRepository.findConfigTime(testUser.getId(), testChatRoom.getId())).thenReturn(null);
		when(chatRepository.findAllByChatRoomId(testChatRoom.getId())).thenReturn(allChats);

		List<ChatResponseDTO> result = chatService.getUnreadMessages(testUser.getId(), testChatRoom.getId());

		assertEquals(expectedResponse, result);
	}
	@Test
	public void testGetInitialMessages() {
		LocalDateTime lastVisitedTime = LocalDateTime.now().minusDays(1);
		LocalDateTime invitationTime = LocalDateTime.now().minusDays(7);

		// Mock 데이터 설정
		List<Chat> unreadChats = Arrays.asList(
			Chat.builder()
				.id(1L)
				.content("Unread Message 1")
				.sendTime(LocalDateTime.now().minusHours(1))
				.user(testUser)
				.chatRoom(testChatRoom)
				.build()
		);

		List<Chat> readChats = Arrays.asList(
			Chat.builder()
				.id(2L)
				.content("Read Message 1")
				.sendTime(LocalDateTime.now().minusDays(2))
				.user(testUser)
				.chatRoom(testChatRoom)
				.build()
		);

		when(userChatRoomRepository.findConfigTime(testUser.getId(), testChatRoom.getId())).thenReturn(lastVisitedTime);
		when(userChatRoomRepository.findInvitationTime(testUser.getId(), testChatRoom.getId())).thenReturn(invitationTime);
		when(chatRepository.findUnreadMessages(testChatRoom.getId(), lastVisitedTime, invitationTime)).thenReturn(unreadChats);
		when(chatRepository.findPreviousMessages(testChatRoom.getId(), lastVisitedTime, invitationTime)).thenReturn(readChats);

		// Expected DTO 리스트 생성
		List<ChatResponseDTO> unreadChatDTOs = unreadChats.stream()
			.map(this::convertToChatResponseDTO)
			.collect(Collectors.toList());

		List<ChatResponseDTO> readChatDTOs = readChats.stream()
			.sorted(Comparator.comparing(Chat::getSendTime))
			.map(this::convertToChatResponseDTO)
			.collect(Collectors.toList());

		InitialChatListResponseDTO expectedResponse = InitialChatListResponseDTO.builder()
			.readMessages(readChatDTOs)
			.unreadMessages(unreadChatDTOs)
			.build();

		// Call the service method
		InitialChatListResponseDTO result = chatService.getInitialMessages(testUser.getId(), testChatRoom.getId());

		// Assertions
		assertEquals(expectedResponse, result);
	}
	@Test
	public void testGetPreviousMessagesBeforeId() {
		Long firstMessageId = 1L;
		LocalDateTime invitationTime = LocalDateTime.now().minusDays(7);

		// Mock 데이터 설정
		List<Chat> previousChats = Arrays.asList(
			Chat.builder()
				.id(2L)
				.content("Previous Message")
				.sendTime(LocalDateTime.now().minusDays(2))
				.user(testUser)
				.chatRoom(testChatRoom)
				.build()
		);

		when(userChatRoomRepository.findInvitationTime(testUser.getId(), testChatRoom.getId())).thenReturn(invitationTime);
		when(chatRepository.findPreviousMessagesBeforeId(testChatRoom.getId(), firstMessageId, invitationTime)).thenReturn(previousChats);

		// Expected DTO 리스트 생성
		List<ChatResponseDTO> expectedDTOs = previousChats.stream()
			.sorted(Comparator.comparing(Chat::getSendTime))
			.map(this::convertToChatResponseDTO)
			.collect(Collectors.toList());

		// Call the service method
		List<ChatResponseDTO> result = chatService.getPreviousMessagesBeforeId(testChatRoom.getId(), firstMessageId, testUser.getId());

		// Assertions
		assertEquals(expectedDTOs, result);
	}
	@Test
	public void testGetNextMessagesAfterId() {
		Long lastMessageId = 1L;
		LocalDateTime invitationTime = LocalDateTime.now().minusDays(7);

		// Mock 데이터 설정
		List<Chat> nextChats = Arrays.asList(
			Chat.builder()
				.id(2L)
				.content("Next Message")
				.sendTime(LocalDateTime.now().plusDays(1))
				.user(testUser)
				.chatRoom(testChatRoom)
				.build()
		);

		when(userChatRoomRepository.findInvitationTime(testUser.getId(), testChatRoom.getId())).thenReturn(invitationTime);
		when(chatRepository.findNextMessagesAfterId(testChatRoom.getId(), lastMessageId, invitationTime)).thenReturn(nextChats);

		// Expected DTO 리스트 생성
		List<ChatResponseDTO> expectedDTOs = nextChats.stream()
			.map(this::convertToChatResponseDTO)
			.collect(Collectors.toList());

		// Call the service method
		List<ChatResponseDTO> result = chatService.getNextMessagesAfterId(testChatRoom.getId(), lastMessageId, testUser.getId());

		// Assertions
		assertEquals(expectedDTOs, result);
	}
	private ChatResponseDTO convertToChatResponseDTO(Chat chat) {
		return ChatResponseDTO.builder()
			.content(chat.getContent())
			.sendTime(chat.getSendTime())
			.sender(chat.getUser().getName())
			.build();
	}
}
