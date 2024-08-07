package com.goorm.insideout.chat.repository.custom;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.goorm.insideout.chat.domain.Chat;
import com.goorm.insideout.chat.domain.QChat;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatQueryDslRepositoryImpl implements ChatQueryDslRepository {

	private final JPAQueryFactory queryFactory;
	private final QChat chat = QChat.chat;

	@Override
	public long countByChatRoomIdAndSendTimeAfter(Long chatRoomId, LocalDateTime time) {
		Long count = queryFactory.select(chat.count())
			.from(chat)
			.where(chat.chatRoom.id.eq(chatRoomId)
				.and(chat.sendTime.gt(time)))
			.fetchOne();

		return count != null ? count : 0L;
	}

	@Override
	public List<Chat> findUnreadMessages(Long chatRoomId, LocalDateTime lastVisitedTime, LocalDateTime invitationTime) {
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(chat.chatRoom.id.eq(chatRoomId));

		if (lastVisitedTime != null) {
			builder.and(chat.sendTime.gt(lastVisitedTime));
		}

		builder.and(chat.sendTime.gt(invitationTime));

		return queryFactory.selectFrom(chat)
			.where(builder)
			.orderBy(chat.sendTime.asc())
			.limit(30)
			.fetch();
	}

	@Override
	public List<Chat> findPreviousMessages(Long chatRoomId, LocalDateTime lastVisitedTime,
		LocalDateTime invitationTime) {
		if (lastVisitedTime == null) {
			return Collections.emptyList();
		}

		BooleanBuilder builder = new BooleanBuilder();

		builder.and(chat.chatRoom.id.eq(chatRoomId));
		builder.and(chat.sendTime.lt(lastVisitedTime));
		builder.and(chat.sendTime.gt(invitationTime));

		return queryFactory.selectFrom(chat)
			.where(builder)
			.orderBy(chat.sendTime.desc())
			.limit(30)
			.fetch();
	}

	@Override
	public List<Chat> findPreviousMessagesBeforeId(Long chatRoomId, Long firstMessageId, LocalDateTime invitationTime) {
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(chat.chatRoom.id.eq(chatRoomId))
			.and(chat.id.lt(firstMessageId))
			.and(chat.sendTime.gt(invitationTime));

		return queryFactory.selectFrom(chat)
			.where(builder)
			.orderBy(chat.sendTime.desc())
			.limit(30)
			.fetch();
	}

	@Override
	public List<Chat> findNextMessagesAfterId(Long chatRoomId, Long lastMessageId, LocalDateTime configTime) {
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(chat.chatRoom.id.eq(chatRoomId))
			.and(chat.sendTime.loe(configTime))
			.and(chat.id.gt(lastMessageId));

		return queryFactory.selectFrom(chat)
			.where(builder)
			.orderBy(chat.sendTime.asc())
			.limit(30)
			.fetch();
	}
}
