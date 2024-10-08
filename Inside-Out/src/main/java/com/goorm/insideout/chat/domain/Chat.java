package com.goorm.insideout.chat.domain;

import static jakarta.persistence.FetchType.*;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.goorm.insideout.chatroom.domain.ChatRoom;
import com.goorm.insideout.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "CHATS")
public class Chat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_id", updatable = false, nullable = false, unique = true)
	private Long id;

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "send_time", nullable = false)
	private LocalDateTime sendTime;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "chat_room_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private ChatRoom chatRoom;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

}
