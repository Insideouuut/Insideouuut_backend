package com.goorm.insideout.chatroom.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.goorm.insideout.club.entity.Club;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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
	private Long id;

	@Column(name = "title", updatable = false, nullable = false)
	private String title;

	@Column(name = "type", nullable = false)
	@Enumerated(EnumType.STRING)
	private ChatRoomType type;

	@Column(name = "associated_id", nullable = false)
	private Long associatedId; // 동아리 또는 모임의 ID

	@Column(name = "last_message_content") // 데이터베이스와 매핑됨
	private String lastMessageContent;

	@Column(name = "last_message_time") // 데이터베이스와 매핑됨
	private LocalDateTime lastMessageTime;

}
