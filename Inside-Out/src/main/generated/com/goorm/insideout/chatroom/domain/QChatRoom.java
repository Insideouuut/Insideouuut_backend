package com.goorm.insideout.chatroom.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatRoom is a Querydsl query type for ChatRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatRoom extends EntityPathBase<ChatRoom> {

    private static final long serialVersionUID = 1021107575L;

    public static final QChatRoom chatRoom = new QChatRoom("chatRoom");

    public final NumberPath<Long> associatedId = createNumber("associatedId", Long.class);

    public final DateTimePath<java.sql.Timestamp> createdAt = createDateTime("createdAt", java.sql.Timestamp.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final SetPath<com.goorm.insideout.chat.domain.Chat, com.goorm.insideout.chat.domain.QChat> messages = this.<com.goorm.insideout.chat.domain.Chat, com.goorm.insideout.chat.domain.QChat>createSet("messages", com.goorm.insideout.chat.domain.Chat.class, com.goorm.insideout.chat.domain.QChat.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    public final EnumPath<ChatRoomType> type = createEnum("type", ChatRoomType.class);

    public QChatRoom(String variable) {
        super(ChatRoom.class, forVariable(variable));
    }

    public QChatRoom(Path<? extends ChatRoom> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChatRoom(PathMetadata metadata) {
        super(ChatRoom.class, metadata);
    }

}

