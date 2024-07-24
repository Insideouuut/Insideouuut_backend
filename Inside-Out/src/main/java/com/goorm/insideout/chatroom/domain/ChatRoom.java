package com.goorm.insideout.chatroom.domain;

import static jakarta.persistence.FetchType.*;

import java.sql.Timestamp;
import java.util.Set;

import com.goorm.insideout.chat.domain.Chat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Setter
@Getter
@Table(name = "CHAT_ROOMS")
public class ChatRoom {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_room_id")
	private long id;

	@Column(name = "title", updatable = false, nullable = false, unique = true)
	private String title;

	@Column(name = "created_at", nullable = false)
	private Timestamp createdAt;

	@Column(name = "type", nullable = false)
	@Enumerated(EnumType.STRING)
	private ChatRoomType type;

	@Column(name = "associated_id", nullable = false)
	private long associatedId; // 동아리 또는 모임의 ID

	@OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, fetch = LAZY)
	private Set<Chat> messages;

}
